version = "1.9.0"

project.extra["PluginName"] = "Chaos Combat Helper"
project.extra["PluginDescription"] = "Various utilities to make combat easier"

dependencies {
    compileOnly(project(":utils"))
    compileOnly(project(":alchemicalhydra"))
    compileOnly(project(":cerberus"))
    compileOnly(project(":demonicgorillas"))
    compileOnly(project(":grotesqueguardians"))
    compileOnly(project(":zulrah"))
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
                                            nameToId("Chaos Utils"),
                                            nameToId("Chaos Alchemical Hydra"),
                                            nameToId("Chaos Cerberus"),
                                            nameToId("Chaos Demonic Gorillas"),
                                            nameToId("Chaos Grotesque Guardians"),
                                            nameToId("Chaos Zulrah")
                                    ).joinToString(),
                            "Plugin-License" to project.extra["PluginLicense"]
                    )
            )
        }
    }
}
