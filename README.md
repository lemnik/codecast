# CodeCast
A crazy idea to render videos of code, written in [Kotlin](http://kotlinlang.org) with [scripts](https://github.com/lemnik/codecast/blob/master/src/main/kotlin/com/lemnik/codecast/scripts/Dummy.kt) written in Kotlin.

This project is relatively half-baked and crazy, written partially because I wanted to see how hard it would be. The scripts for the videos are defined as part of the code-base, but moving them into Kotlin-scripts looks like it should be relatively easy if I ever decide I have the time for it.

![](Dummy.gif?raw=true)

## Major Issues still outstanding

* Scenes require lots of duplication
* The scripts are part of the codebase (should be using `javax.script` to import external Kotlin scripts)
* Only Java highlighting is supported
* Highlighting is based purely on words, it's not real syntax highlighting
* More animation structures should really exist
* __The code was written in a hurry, just to see how hard this would be__


