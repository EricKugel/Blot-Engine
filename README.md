# Blot-Engine 

This is an engine to be used with [hackclub blot project](https://github.com/hackclub/blot). It is software written in Java to produce (from code, images, text, etc) and draw drawings with more freedom than the current editor provides. This project will be incorporating the work of several contributors with their permission, as well as creating new code/images/text->drawing engines.

## Anticipated Features
 - The ability to move, scale, copy/paste, etc "DrawingObjects" (like Blot polylines) on a MS paint-like canvas. This would, for example, allow you to position some text below an image, or scale an image to fit in the drawing area.
 - Create DrawingObjects through various default plugins. These plugins will be separated by source (text, image, etc), and there could possibly be a great number of plugins for each. (There's infinitely many artistic or realistic ways to turn an image into a drawing.)
 - Expanded blot font: more text variety using svg font files?

## Current capabilities:
 - Connect to a Blot machine using jSerialComm and send simple move & pen instructions.

Remember to add jSerialComm to the classpath before running.