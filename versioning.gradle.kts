fun getCurrentVersion(project: Project): String {
    var patch = System.getenv("GITHUB_RUN_NUMBER")
    var branch = System.getenv("GITHUB_REF")
    if (branch != null && branch.startsWith("refs/heads/")) {
        branch = branch.substring("refs/heads/".length)

        // remove feat/ prefix
        if (branch.startsWith("feat/")) {
            branch = branch.substring("feat/".length)
        }

        // replace / with -
        branch = branch.replace("/", "-")
        if (branch == "main" || branch == "release") {
            branch = null
        }
    }
    return "${project.property("mod_version")}" +
            if (patch != null) ".$patch" else "" +
                    "-${project.property("minecraft_version")}" +
                    "-${project.property("mod_version_qualifier")}" +
                    if (branch != null) "-$branch" else ""
}

// Expose the function to the project
project.extensions.extraProperties["getCurrentVersion"] = { getCurrentVersion(project) }
