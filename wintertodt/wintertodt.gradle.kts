version = "1.0.0"

project.extra["PluginName"] = "Chaos Wintertodt"
project.extra["PluginDescription"] = "The cold of the Wintertodt seeps into your bones"

dependencies {
    compileOnly(group = "io.reisub.unethicalite", name = "utils", version = utilsVersion)
}

tasks {
    jar {
        manifest {
            attributes(
                mapOf(
                    "Plugin-Version" to project.version,
                    "Plugin-Id" to nameToId(project.extra["PluginName"] as String),
                    "Plugin-Provider" to project.extra["PluginProvider"],
                    "Plugin-Description" to project.extra["PluginDescription"],
                    "Plugin-Dependencies" to
                            arrayOf(
                                nameToId("Chaos Utils")
                            ).joinToString(),
                    "Plugin-License" to project.extra["PluginLicense"]
                )
            )
        }
    }
}
