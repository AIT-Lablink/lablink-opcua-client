from opcua import ua, Server
from opcua.server.user_manager import UserManager
import code

# User database.
users_db = {
    'LablinkTestReader': 'vOBKtPzcWv',
    'LablinkTestWriter': 'upDAOaPzsJ',
}

# Callback function for user access management.
def user_manager(isession, username, password):
    isession.user = UserManager.User
    login_ok = username in users_db and password == users_db[username]
    print('Login attempt with username "{}": {}'.format(username, 'SUCCESS' if login_ok else 'FAILED'))
    return login_ok

# Main routine.
if __name__ == '__main__':

    server = Server()

    server.set_server_name('Lablink Test Server')
    server.set_endpoint('opc.tcp://localhost:12345/lablink-test')

    server.set_security_policy([ua.SecurityPolicyType.NoSecurity])

    server.set_security_IDs(['Username'])
    server.user_manager.set_user_manager(user_manager)

    idx = server.register_namespace('urn:lablink:opcua-test')

    folder_lablink_test = server.nodes.objects.add_folder(idx, 'LablinkTest')
    folder_scalar_types = folder_lablink_test.add_folder(idx, 'ScalarTypes')

    scalar_var_defs = [
        ['LlTestBoolean', True, ua.VariantType.Boolean],
        ['LlTestSByte', 127, ua.VariantType.SByte],
        ['LlTestByte', 244, ua.VariantType.Byte],
        ['LlTestInt16', 16, ua.VariantType.Int16],
        ['LlTestUInt16', 16, ua.VariantType.UInt16],
        ['LlTestInt32', 32, ua.VariantType.Int32],
        ['LlTestUInt32', 32, ua.VariantType.UInt32],
        ['LlTestInt64', 64, ua.VariantType.Int64],
        ['LlTestUInt64', 64, ua.VariantType.UInt64],
        ['LlTestFloat', 3.1415, ua.VariantType.Float],
        ['LlTestDouble', 2.7182, ua.VariantType.Double],
        ['LlTestString', 'Lablink', ua.VariantType.String],
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