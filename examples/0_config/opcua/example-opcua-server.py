from opcua import ua, Server
import code

if __name__ == "__main__":
    server = Server()
    server.set_endpoint("opc.tcp://localhost:12345/lablink-test")
    server.set_server_name("Lablink Test Server")
    server.set_security_policy([ua.SecurityPolicyType.NoSecurity])

    idx = server.register_namespace("urn:lablink:opcua-test")

    folder_lablink_test = server.nodes.objects.add_folder(idx, "LablinkTest")
    folder_scalar_types = folder_lablink_test.add_folder(idx, "ScalarTypes")
   
    scalar_var_defs = [
        ["LlTestBoolean", True, ua.VariantType.Boolean],
        ["LlTestSByte", 127, ua.VariantType.SByte],
        ["LlTestByte", 244, ua.VariantType.Byte],
        ["LlTestInt16", 16, ua.VariantType.Int16],
        ["LlTestUInt16", 16, ua.VariantType.UInt16],
        ["LlTestInt32", 32, ua.VariantType.Int32],
        ["LlTestUInt32", 32, ua.VariantType.UInt32],
        ["LlTestInt64", 64, ua.VariantType.Int64],
        ["LlTestUInt64", 64, ua.VariantType.UInt64],
        ["LlTestFloat", 3.1415, ua.VariantType.Float],
        ["LlTestDouble", 2.7182, ua.VariantType.Double],
        ["LlTestString", "Lablink", ua.VariantType.String],
    ]
    
    str_nodeid_stub = 'ns={};s=LablinkTest/ScalarTypes/{};'
    
    for var_def in scalar_var_defs:
        str_nodeid = str_nodeid_stub.format(idx, var_def[0])
        var = folder_scalar_types.add_variable(str_nodeid, var_def[0], var_def[1], var_def[2])
        var.set_writable()
    
    server.start()

    try:
        myvars = globals()
        myvars.update(locals())
        shell = code.InteractiveConsole(myvars)
        shell.interact()
    finally:
        server.stop()