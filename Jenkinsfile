node {
    // Get Artifactory server instance, defined in the Artifactory Plugin administration page.
    def server = Artifactory.server "dftbluebadge"
    // Create an Artifactory Gradle instance.
    def rtGradle = Artifactory.newGradleBuild()

    stage('Clone sources') {
      git(
           url: 'git@github.com:uk-gov-dft/usermanagement-service.git',
           credentialsId: 'githubsshkey',
           branch: "${branch}"
        )
    }

    stage 'Artifactory configuration'
        // Tool name from Jenkins configuration
        rtGradle.tool = "Gradle-4.7"
        // Set Artifactory repositories for dependencies resolution and artifacts deployment.
        rtGradle.deployer repo:'gradle-release-local', server: server

    stage 'Gradle build'
        def buildInfo = rtGradle.run rootDir: "", buildFile: 'build.gradle', tasks: 'clean artifactoryPublish'

    stage 'Publish build info'
        server.publishBuildInfo buildInfo
}
