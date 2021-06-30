db.createUser({
    user: "root", pwd: "root", roles: [
        { role: "root", db: "admin" },
        { role: "userAdminAnyDatabase", db: "admin" },
        { role: "dbAdminAnyDatabase", db: "admin" },
        { role: "dbOwner", db: "admin" },
        { role: "userAdmin", db: "admin" }
    ]
})

db.runCommand(
    {
        grantRolesToUser: "root",
        roles:
            [
                { role: "userAdmin", db: "admin" }
            ]
    }
)

db.runCommand({ rolesInfo: 1, showBuiltinRoles: true })


db.getUser("root")