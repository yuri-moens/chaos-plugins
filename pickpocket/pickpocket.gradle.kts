version = "1.0.3"

project.extra["PluginName"] = "Chaos Pickpocket"
project.extra["PluginDescription"] = "Cor blimey mate, what are ye doing in me pockets?"

dependencies {
    compileOnly(project(":utils"))
}

tasks {
    jar {
        manifest {
            attributes(mapOf(
                "Plugin-Version" to project.version,
                "Plugin-Id" to nameToId(project.extra["PluginName"] as String),
                "Plugin-Provider" to project.extra["PluginProvider"],
                "Plugin-Description" to project.extra["PluginDescription"],
                "Plugin-Dependencies" to
                        arrayOf(
                            nameToId("Chaos Utils")
                        ).joinToString(),
                "Plugin-License" to project.extra["PluginLicense"]
            ))
        }
    }
}
