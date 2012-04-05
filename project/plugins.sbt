addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.7.3")

addSbtPlugin("ch.craven" %% "scct-plugin" % "0.2")

resolvers += Resolver.url("sbt-plugin-releases",
  new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)
