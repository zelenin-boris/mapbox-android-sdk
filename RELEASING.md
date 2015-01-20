# Releasing

1. Choose a version number per [Semantic Versioning](http://semver.org/). Let's call it `x.y.z`.
1. Create a release ticket with release highlights and release steps

```
- [ ] Update HTTP User Agent Version in MapboxConstants.java
- [ ] Publish `x.y.z` artifact to Maven Central
- [ ] Updating Mapbox.com Documentation
- [ ] Tag `x.y.z` in GitHub
- [ ] Update SDK's `build.gradle` to `x.y+1.z-SNAPSHOT`
- [ ] Update README to list current and snapshot build numbers
- [ ] Update Demo Project to use new `x.y.z` artifact from Maven Central
- [ ] Push new updated Demo App to Google Play
- [ ] Move non completed issues from `x.y.z` to `x.y+1.z` milestone
```
