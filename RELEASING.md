# Releasing

1. Choose a version number per [Semantic Versioning](http://semver.org/). Let's call it `x.y.z`.
1. Create a release ticket with release highlights and release steps

```
- [ ] Update HTTP User Agent Version in MapboxConstants.java
- [ ] Publish `x.y.z` artifact to Central Repository
- [ ] Update README to list current and snapshot build numbers
- [ ] Update Mapbox.com Documentation (requires `x.y.z` to be found on http://search.maven.org - usually a few hours after release)
- [ ] Tag `x.y.z` in GitHub
- [ ] Update SDK's `build.gradle` to `x.y+1.z-SNAPSHOT`
- [ ] Update Demo Project to use new `x.y.z` artifact from Maven Central
- [ ] Push new updated Demo App to Google Play
- [ ] Move non completed issues from `x.y.z` to `x.y+1.z` milestone
```

## Publishing To Central Repository
1. Add Nexus Repository and Signing Credentials To /MapboxAndroidSDK/gradle.properties (DO NOT COMMIT)
1. In Terminal
  ```sh
  cd MapboxAndroidSDK
  ./deploy.sh
  ```
1. Follow [instructions](http://central.sonatype.org/pages/releasing-the-deployment.html) to Close Staging Repository and Release To Central Repository
1. Remove Nexus Repository and Signing Credentials From /MapboxAndroidSDK/gradle.properties

## Updating Mapbox.com Documentation
1. Follow instructions in `/docs/README.md`
