version = "1.8.2"

project.extra["PluginName"] = "Chaos Combat Helper"
project.extra["PluginDescription"] = "Various utilities to make combat easier"

dependencies {
    compileOnly(project(":utils"))
    compileOnly(group = "com.openosrs.externals", name = "cerberus", version = "5.0.2")
    compileOnly(group = "com.openosrs.externals", name = "zulrah", version = "5.0.7")
    compileOnly(group = "com.openosrs.externals", name = "grotesqueguardians", version = "5.0.4")
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
                                            nameToId("Cerberus"),
                                            nameToId("Zulrah"),
                                            nameToId("Grotesque Guardians")
                                    ).joinToString(),
                            "Plugin-License" to project.extra["PluginLicense"]
                    )
            )
        }
    }
}
