rootProject.name = "ForBuilder"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven(url = "https://repo.caramel.moe/repository/maven-public/") // caramel-repo
        maven(url = "https://papermc.io/repo/repository/maven-public/") // papermc-repo
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // only use these repos
}