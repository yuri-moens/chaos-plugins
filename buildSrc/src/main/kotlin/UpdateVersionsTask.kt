import okhttp3.OkHttpClient
import okhttp3.Request
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.get
import org.json.JSONArray
import org.json.JSONObject
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest


open class UpdateVersionsTask : DefaultTask() {

    private fun hash(file: ByteArray): String {
        return MessageDigest.getInstance("SHA-512").digest(file).fold("", { str, it -> str + "%02x".format(it) })
            .toUpperCase()
    }

    private fun getBootstrap(): JSONArray? {
        val client = OkHttpClient()
        val url = "https://raw.githubusercontent.com/${project.rootProject.extra.get("GithubUserName")}/${
            project.rootProject.extra.get(
                "GithubRepoName"
            )
        }/master/plugins.json"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute()
            .use { response -> return JSONObject("{\"plugins\":${response.body!!.string()}}").getJSONArray("plugins") }
    }

    private fun readFile(fileName: Path): List<String> = fileName.toFile().useLines { it.toList() }

    private fun writeFile(fileName: Path, content: List<String>) =
        fileName.toFile().writeText(content.joinToString(separator = System.lineSeparator()))

    private fun bumpVersion(path: Path) {
        val content = mutableListOf<String>()

        readFile(path).forEach {
            if (it.startsWith("version = ")) {
                val version = SemVer.parse(it.replace("\"", "").replace("version = ", ""))
                version.patch += 1
                content.add("version = \"$version\"")
            } else {
                content.add(it)
            }
        }

        if (content.size > 0) {
            writeFile(path, content)
        }
    }

    @TaskAction
    fun version() {
        if (project == project.rootProject) {
            val baseBootstrap = getBootstrap() ?: throw RuntimeException("Base bootstrap is null!")

            project.subprojects.forEach {
                if (it.project.properties.containsKey("PluginName") && it.project.properties.containsKey("PluginDescription")) {
                    val plugin = it.project.tasks.get("jar").outputs.files.singleFile
                    val hash = hash(plugin.readBytes())

                    for (i in 0 until baseBootstrap.length()) {
                        val item = baseBootstrap.getJSONObject(i)

                        if (!item.get("id").equals(nameToId(it.project.extra.get("PluginName") as String))) {
                            continue
                        }

                        if (hash in item.getJSONArray("releases").toString()) {
                            continue
                        }
                        val build = Paths.get("${it.projectDir}", "${it.project.name}.gradle.kts")

                        if (Files.exists(build)) {
                            bumpVersion(build)
                        }
                    }
                }
            }
        }

    }
}