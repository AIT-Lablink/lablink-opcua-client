//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
//
// This program and the accompanying materials are made
// available under the terms of the Eclipse Public License 2.0
// which is available at: https://www.eclipse.org/legal/epl-2.0/
//

package at.ac.ait.lablink.clients.opcuaclient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.regex.Pattern;

class KeyStoreLoader {

  private static final Pattern IP_ADDR_PATTERN = Pattern.compile(
      "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$"
  );

  private static final String CLIENT_ALIAS = "lablink-opcuaclient"; // FIXME: make configurable
  private static final char[] PASSWORD = "password".toCharArray(); // FIXME: make configurable

  private final Logger logger = LogManager.getLogger(KeyStoreLoader.class);

  private X509Certificate clientCertificate;
  private KeyPair clientKeyPair;

  KeyStoreLoader load(Path baseDir) throws Exception {

    KeyStore keyStore = KeyStore.getInstance("PKCS12"); // FIXME: make configurable

    Path serverKeyStore = baseDir.resolve("lablink-opcuaclient.pfx"); // FIXME: make configurable

    logger.info("Loading KeyStore at {}", serverKeyStore);

    if (!Files.exists(serverKeyStore)) {
      keyStore.load(null, PASSWORD);

      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

      SelfSignedCertificateBuilder builder = new SelfSignedCertificateBuilder(keyPair)
          .setCommonName("Lablink OPC UA client") // FIXME: make configurable
          .setOrganization("AIT") // FIXME: make configurable
          .setLocalityName("Vienna") // FIXME: make configurable
          .setCountryCode("AT") // FIXME: make configurable
          .setApplicationUri("urn:lablink:clients:opcuaclient") // FIXME: make configurable
          .addDnsName("localhost") // FIXME: make configurable
          .addIpAddress("127.0.0.1"); // FIXME: make configurable

      // Get as many hostnames and IP addresses as we can listed in the certificate.
      for (String hostname : HostnameUtil.getHostnames("0.0.0.0")) {
        if (IP_ADDR_PATTERN.matcher(hostname).matches()) {
          builder.addIpAddress(hostname);
        } else {
          builder.addDnsName(hostname);
        }
      }

      X509Certificate certificate = builder.build();

      keyStore.setKeyEntry(CLIENT_ALIAS, keyPair.getPrivate(), 
          PASSWORD, new X509Certificate[]{certificate});

      try (OutputStream out = Files.newOutputStream(serverKeyStore)) {
        keyStore.store(out, PASSWORD);
      }
    } else {
      try (InputStream in = Files.newInputStream(serverKeyStore)) {
        keyStore.load(in, PASSWORD);
      }
    }

    Key serverPrivateKey = keyStore.getKey(CLIENT_ALIAS, PASSWORD);
    if (serverPrivateKey instanceof PrivateKey) {
      clientCertificate = (X509Certificate) keyStore.getCertificate(CLIENT_ALIAS);
      PublicKey serverPublicKey = clientCertificate.getPublicKey();
      clientKeyPair = new KeyPair(serverPublicKey, (PrivateKey) serverPrivateKey);
    }

    return this;
  }

  X509Certificate getClientCertificate() {
    return clientCertificate;
  }

  KeyPair getClientKeyPair() {
    return clientKeyPair;
  }

}
