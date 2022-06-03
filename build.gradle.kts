import ProjectVersions.openosrsVersion

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
apply<VersionPlugin>()
apply<UpdateVersionsPlugin>()

allprojects {
    group = "io.reisub"
    version = ProjectVersions.rlVersion
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
    apply<JavaLibraryPlugin>()
    apply<CheckstylePlugin>()

    configure<CheckstyleExtension> {
        toolVersion = "10.1"
        maxWarnings = 0
        isShowViolations = true
        isIgnoreFailures = false
    }

    repositories {
        jcenter {
            content {
                excludeGroupByRegex("com\\.openosrs.*")
                excludeGroupByRegex("com\\.runelite.*")
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
        compileOnly("com.openosrs:http-api:$openosrsVersion+")

        compileOnly(Libraries.guice)
        compileOnly(Libraries.javax)
        compileOnly(Libraries.lombok)
        compileOnly(Libraries.pf4j)
        compileOnly(Libraries.apacheCommonsText)
        compileOnly(Libraries.okhttp3)
        compileOnly(Libraries.gson)
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

        withType<Checkstyle> {
            group = "verification"

            exclude("**/Location.java")
        }

        withType<AbstractArchiveTask> {
            isPreserveFileTimestamps = false
            isReproducibleFileOrder = true
            dirMode = 493
            fileMode = 420
        }
    }
}

tasks {
    register<Delete>("bootstrapClean") {
        delete("release/")
    }
}
