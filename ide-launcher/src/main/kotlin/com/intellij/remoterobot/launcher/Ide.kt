package com.intellij.remoterobot.launcher

enum class Ide(val code: String, val feedsCode: String) {
    IDEA_COMMUNITY("IC", "IIC"),
    IDEA_ULTIMATE("IU", "IIU"),
    CLION("CL", "CL"),
    WEBSTORM("WS", "WS"),
    RUBY_MINE("RM", "RM"),
    PYCHARM("PY", "PCP"),
    PYCHARM_COMMUNITY("PC", "PCC");

    fun getIdePropertiesEnvVarName() = when (this) {
        IDEA_COMMUNITY, IDEA_ULTIMATE -> "IDEA_PROPERTIES"
        CLION -> "CLION_PROPERTIES"
        WEBSTORM -> if (Os.hostOS() == Os.MAC) "WEBSTORM_PROPERTIES" else "WEBIDE_PROPERTIES"
        RUBY_MINE -> "RUBYMINE_PROPERTIES"
        PYCHARM, PYCHARM_COMMUNITY -> "PYCHARM_PROPERTIES"
    }

    fun getVmOptionsEnvVarName() = when (this) {
        IDEA_COMMUNITY, IDEA_ULTIMATE -> "IDEA_VM_OPTIONS"
        CLION -> "CLION_VM_OPTIONS"
        WEBSTORM -> if (Os.hostOS() == Os.MAC) "WEBSTORM_VM_OPTIONS" else "WEBIDE_VM_OPTIONS"
        RUBY_MINE -> "RUBYMINE_VM_OPTIONS"
        PYCHARM, PYCHARM_COMMUNITY -> "PYCHARM_VM_OPTIONS"
    }.let {
        if (Os.hostOS() == Os.WINDOWS) {
            val productPrefix = it.substringBefore("_")
            "${productPrefix}64${it.removePrefix(productPrefix)}"
        } else {
            it
        }
    }

    enum class BuildType(val title: String) {
        EAP("eap"), RELEASE("release")
    }

    companion object {
        fun withCode(code: String) = values().single { it.code == code }
    }
}
