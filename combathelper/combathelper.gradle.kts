version = "1.1.4"

project.extra["PluginName"] = "Chaos Combat Helper"
project.extra["PluginDescription"] = "Various utilities to make combat easier"

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
