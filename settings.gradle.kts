/*
 * Copyright (c) 2019 Owain van Brakel <https:github.com/Owain94>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

rootProject.name = "Chaos Plugins"

include("agility")
include("autodropper")
include("bankpin")
include("barrows")
include("birdhouse")
include("blastfurnace")
include("cerberus")
include("chinchompas")
include("chompychomper")
include("cluepuzzlesolver")
include("combathelper")
include("cooking")
include("daeyaltessence")
include("demonicgorillas")
include("dialogcontinue")
include("enchanter")
include("farming")
include("funguspicker")
include("glassblower")
include("grotesqueguardians")
include("herblore")
include("leftclickcast")
include("mahoganyhomes")
include("mining")
include("motherlodemine")
include("mta")
include("nmz")
include("pickpocket")
include("plankmaker")
include("pyramidplunder")
include("secondarygatherer")
include("shopper")
include("smithing")
include("sulliuscep")
include("superglassmake")
include("tabmaker")
include("tempoross")
include("thermyflicker")
include("utils")
include("twotickteaks")
//include("wintertodt")
include("zmi")
include("zulrah")

if (File("./test").exists()) {
    include("test")
}

for (project in rootProject.children) {
    project.apply {
        projectDir = file(name)
        buildFileName = "$name.gradle.kts"

        require(projectDir.isDirectory) { "Project '${project.path} must have a $projectDir directory" }
        require(buildFile.isFile) { "Project '${project.path} must have a $buildFile build script" }
    }
}
