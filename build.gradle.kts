import ProjectVersions.openosrsVersion
import groovy.xml.dom.DOMCategory.attributes

buildscript {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    `java-library`
    checkstyle
}

project.extra["GithubUrl"] = "https://github.com/yuri-moens/chaos-plugins"
project.extra["GithubUserName"] = "yuri-moens"
project.extra["GithubRepoName"] = "chaos-plugins"

apply<BootstrapPlugin>()

allprojects {
    group = "io.reisub"
    apply<MavenPublishPlugin>()

    repositories {
        mavenLocal()
    }
}

subprojects {
    group = "io.reisub.unethicalite"

    project.extra["PluginProvider"] = "ChaosEnergy"
    project.extra["ProjectSupportUrl"] = "https://github.com/yuri-moens/chaos-plugins/issues"
    project.extra["PluginLicense"] = "3-Clause BSD License"

    apply<JavaPlugin>()
    apply(plugin = "java-library")
//    apply(plugin = "checkstyle")

    repositories {
        jcenter {
            content {
                excludeGroupByRegex("com\\.openosrs.*")
            }
        }

        exclusiveContent {
            forRepository {
                mavenLocal()
            }

            filter {
                includeGroupByRegex("com\\.openosrs.*")
            }
        }
    }

    dependencies {
        annotationProcessor(Libraries.lombok)
        annotationProcessor(Libraries.pf4j)

        compileOnly("com.openosrs:runelite-api:$openosrsVersion+")
        compileOnly("com.openosrs:runelite-client:$openosrsVersion+")

        compileOnly(Libraries.guice)
        compileOnly(Libraries.javax)
        compileOnly(Libraries.lombok)
        compileOnly(Libraries.pf4j)
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    configure<PublishingExtension> {
        repositories {
            maven {
                url = uri("$buildDir/repo")
            }
        }
        publications {
            register("mavenJava", MavenPublication::class) {
                from(components["java"])
            }
        }
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        withType<AbstractArchiveTask> {
            isPreserveFileTimestamps = false
            isReproducibleFileOrder = true
            dirMode = 493
            fileMode = 420
        }

        withType<Jar> {
            doLast {
                copy {
                    from("./build/libs/")
                    into("../release/")
                }
            }
        }
    }
}
