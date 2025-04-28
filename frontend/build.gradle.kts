/*import com.vidal.gradle.plugin.angular.AngularExtension

plugins {
  alias(libs.plugins.angular)
  base
}

angular {
  cliVersion.set("16.0.0")
  withPrettier()
}

tasks.generateAngularApp {
  args.add("--standalone")
}

val angularDist by configurations.creating {
  description = "Transpiled static resources"
}

val zip by tasks.registering(Zip::class) {
  archiveBaseName.set("frontend-dist")
  from(file(project.the<AngularExtension>().buildDir))
  dependsOn(tasks.ngBuild)
}
artifacts.add(angularDist.name, zip)

val format by tasks.registering {
  dependsOn(tasks.named("npmPrettier"))
}

tasks.ngTest {
  inputs.file(file("jest.config.ts"))
  args.addAll("--ci", "--coverage")
}

val coverageExclusions =
  listOf(
    "src/app/**/*.spec.ts",
    "src/app/**/*.module.ts",
  )

sonarqube {
  properties {
    property("sonar.sources", "src/app")
    property("sonar.javascript.lcov.reportPaths", "${project.the<AngularExtension>().testReport.coverageOutputDir.get()}/lcov.info")
    property("sonar.coverage.exclusions", coverageExclusions)
    property("sonar.nodejs.executable", "${project.the<AngularExtension>().srcDir.get()}/nodew")
  }
}
*/

plugins {
  id("com.github.node-gradle.node") version "7.0.2"
  base
}

node {
  version.set("20.12.0") // adapte à la version de ton projet
  npmVersion.set("10.5.0")
  download.set(true) // télécharge Node automatiquement
  workDir.set(file("${project.buildDir}/nodejs"))
  npmWorkDir.set(file("${project.buildDir}/npm"))
}

tasks.register<com.github.gradle.node.npm.task.NpmTask>("npmStart") {
  workingDir = file("src")
  dependsOn(tasks.named("npmInstall"))
  args.set(listOf("run", "start"))
}
