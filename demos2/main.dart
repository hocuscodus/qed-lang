import 'dart:async';

import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'src/qed/lang/Event.dart';
import 'src/qed/lang/declaration/LambdaDeclaration.dart';
import 'src/qed/lang/Obj.dart';
import 'src/qed/lang/QEDProcess.dart';
import 'src/qed/lang/returnhandler/ProcessReturnHandler.dart';
import 'src/qed/ui/Node.dart';
import 'dart:typed_data';
//import 'package:http/http.dart' as http;
import 'dart:ui' as ui;
import 'dart:js';
import 'dart:convert';
import 'QEDTextField.dart';

//import 'package:flutter/services.dart' show rootBundle;

void main() {
//  executeApp("AAMAFAMABAEA/wYCAwAEAgD/BgIAAAECABEEAQAGcmV0dXJuAAEAAQAAAQABBAAPX2xpYi5saWIuc3RkbGliAAMAJwECAQQAAAABAgECAQQAAAACAgMABAABAwAEAQEhAgEEAAIBBAACAAAnEgEABnJldHVybgAAAAEAAAEAAAQABk9iamVjdAAAAAAAAAMBAAZyZXR1cm4AAAABAAABAAAEAAlnZXROdW1EaW0AAAABAAABAQEABnJldHVybgABAAEAAAEAAQAEAAh0b1N0cmluZwAAAAEAAAEBAQAGcmV0dXJuAAEAAQAAAQABAAAEAARib29sAAAAAAAAAQEABnJldHVybgAAAAEAAAEAAAAEAARieXRlAAAAAQAAAQEBAAZyZXR1cm4AAAABAAABAAAABAADaW50AAAAAAAAAQEABnJldHVybgAAAAEAAAEAAAAEAAVmbG9hdAAAAAAAAAEBAAZyZXR1cm4AAAABAAABAAAABAAGU3RyaW5nAAAAAAAAAQEABnJldHVybgAAAAEAAAEAAAAEAAJpZgABAAEAAAEBAQAGcmV0dXJuAAEAAQAAAQABAQQABGVsc2UAAAABAAABAQEABnJldHVybgABAAEAAAEAAQAEAAZzd2l0Y2gAAQABAAABAQEABnJldHVybgABAAEAAAEAAQEEAARjYXNlAAEAAQAAAQEBAAZyZXR1cm4AAQABAAABAAEBBAAHZGVmYXVsdAAAAAEAAAEBAQAGcmV0dXJuAAEAAQAAAQABAAQAB3ByaW50bG4AAQABAAABAQEABnJldHVybgAAAAEAAAEAAAEEAAVwcmludAABAAEAAAEBAQAGcmV0dXJuAAAAAQAAAQAAAQQABVRpbWVyAAIACgECAQQAAAAAAgAACgIBAAZyZXR1cm4AAAABAAABAAAEAAVyZXNldAAAAAEAAAEBAQAGcmV0dXJuAAAAAQAAAQAAAAEEAAZDb0xpc3QAAQAKAQIBBAAAAAACAAAKBQEABnJldHVybgAAAAEAAAEAAAQAA2VuZAAAAAEAAAEBAQAGcmV0dXJuAAAAAQAAAQAAAAQABnJlbW92ZQABAAEAAAEBAQAGcmV0dXJuAAEAAQAAAQABAQQABXlpZWxkAAAAAQAAAQEBAAZyZXR1cm4AAQABAAABAAEABAAHcHJvY2VzcwAAAAEAAAEBAQAGcmV0dXJuAAEAAQAAAQABAAAEAARyZWN0AAAAAQAAAQEBAAZyZXR1cm4AAAABAAABAAAABAAEb3ZhbAAAAAEAAAEBAQAGcmV0dXJuAAAAAQAAAQAAAAAEAAVfdGVzdAAAAAEAAAECAQAGcmV0dXJuAAAAAQAAAQAAAAACJDEAAQAxAQIBBAAAAAABAgEEAAAAAB4CAQQAAgEEACUACwECAQYDbW1tCAAIAQIBBgNNTU0CAAAxAAAAAAhfb3V0XzE2OQABAAgDAQQCAQIAAAAHAAEB/wD/AAAAAAD/AP8A/wD/AAABAAIA/wAAAAAA/wD/AP8A/wABAQECAAADb3V0AwD//wEAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB/wD/AAAAAAD/AP8A/wD/AAABAP8A/wAAAAAA/wD/AP8A/wABAQIBBgAAA291dAEA//8BAAEAAAA=");
//  executeApp("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");
  context["runQED"]= (String code) {
    executeApp(code);
  };
  context.callMethod('triggerEvent');
}

void executeApp(String codeText) {
  var code = base64.decode(codeText);

  // wait forever if it gets there
  runApp(MyApp(code));
}

void exitSystem(int value) {
//  exit(value);
}

class MyApp extends StatelessWidget {
  Uint8List code;

  MyApp(this.code);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        body: QEDCanvas(context, code),
      ),
    );
/*
    return MaterialApp(
      title: 'QED/Flutter',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or simply save your changes to "hot reload" in a Flutter IDE).
        // Notice that the counter didn't reset back to zero; the application
        // is not restarted.
        primarySwatch: Colors.blue,
        // This makes the visual density adapt to the platform that you run
        // the app on. For desktop platforms, the controls will be smaller and
        // closer together (more dense) than on mobile platforms.
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: MyHomePage(title: 'QED/Flutter'),
    );*/
  }
}

class QEDCanvas extends StatefulWidget {
  BuildContext context;
  Uint8List code;

  QEDCanvas(BuildContext this.context, Uint8List this.code);

  @override
  QEDState createState() => QEDState(code);
}

class QEDState extends State<QEDCanvas> {
//  ui.Image _image;
  Uint8List code;
  DartQEDProcess process;
  List<int> mousePos;

  final TextEditingController _controller = new TextEditingController();

  QEDState(this.code);

  @override
  void initState() {
//    List.generate(widget.numberOfParticles, (index) {
//    });
    super.initState();
//    _loadImage();
    DartQEDProcess(this).runWithHandler(["out.qbc"], DartProcessReturnHandler(this));
  }
/*
  _loadImage() async {
    ByteData bd = await rootBundle.load("images/qedLogo.png");
    final Uint8List image = Uint8List.view(bd.buffer);
//    final ui.Codec codec = await ui.instantiateImageCodec(bytes);
//    final ui.Image image = (await codec.getNextFrame()).image;
//    setState(() => _image = image);
    final Completer<ui.Image> completer = new Completer();
    ui.decodeImageFromList(image, (ui.Image img) {
      setState(() {
        _isApploaded = true;
      });
      return completer.complete(img);
    });
    return completer.future;
  }
*/

  bool processEvent(int modifier, List<int> location) {
    Event qedEvent = modifier != -1 ? getevent(modifier, null) : null;

    //    	log3("Dragged " + event.getX() + " " + event.getY() + " " + event.getModifiers() + " " + event.getModifiersEx());
    return (process.processEventWithModifier(location, modifier, qedEvent));
  }

  Event getevent(int modifier, Object event) {
    //            AWTEvent message = (AWTEvent) event;
    Event msgevent = null;

    switch (modifier) {
      case Node.MODIFonclick:
      case Node.MODIFonpress:
      case Node.MODIFonrelease:
      case Node.MODIFondrag:
      case Node.MODIFonhover:
      case Node.MODIFonhoverin:
      case Node.MODIFonhoverout:
        msgevent = MouseEvent();
        break;
    }

    return (msgevent);
  }

  @override
  Widget build(BuildContext context) {/*Scaffold(
          appBar: AppBar(
          title: Text('Sketcher'),
    ),
    body:*/
    if (process != null) {
//      return Center(child: Text('QED/Flutter: loaded'));
      Widget mainCanvas = GestureDetector(
          onTapDown: (TapDownDetails details) {
            setState(() {
              Offset point = details.localPosition;
//              RenderBox box = context.findRenderObject();
//              Offset point = box.globalToLocal(details.globalPosition);
//              point = point.translate(0.0, -(AppBar().preferredSize.height));

              mousePos = [point.dx.toInt(), point.dy.toInt()];
              processEvent(Node.MODIFonpress, mousePos);
            });
          },
          onTapUp: (TapUpDetails details) {
            setState(() {
              Offset point = details.localPosition;

              mousePos = [point.dx.toInt(), point.dy.toInt()];
              processEvent(Node.MODIFonrelease, mousePos);
            });
          },
          onPanStart: (DragStartDetails details) {
            setState(() {
              Offset point = details.localPosition;

              mousePos = [point.dx.toInt(), point.dy.toInt()];
              processEvent(Node.MODIFondrag, mousePos);
            });
          },
          onPanUpdate: (DragUpdateDetails details) {
            setState(() {
              Offset point = details.localPosition;

              mousePos = [point.dx.toInt(), point.dy.toInt()];
              processEvent(Node.MODIFondrag, mousePos);
            });
          },
          onPanEnd: (DragEndDetails details) {
            processEvent(Node.MODIFondrag, mousePos);
          },
//          onTap: _handleTap,
          child: MouseRegion(
              child:
              Container(
                  margin: EdgeInsets.all(1.0),
                  alignment: Alignment.topLeft,
                  color: Colors.blueGrey[50],
                  child: CustomPaint(
                      painter: QEDPainter(process)
                  )
              ),
              onHover: (PointerHoverEvent event) =>
                  print("Hovering at " + event.position.dy.toString() + " " + event.position.dx.toString())
/*                  setState(() {
                    Offset point = event.position;

                    mousePos = [point.dx.toInt(), point.dy.toInt()];
                    processEvent(Node.MODIFonhover, mousePos);
                  })*//*,
              onEnter: (PointerEnterEvent event) =>
                  setState(() {
                    Offset point = event.position;

                    mousePos = [point.dx.toInt(), point.dy.toInt()];
                    processEvent(Node.MODIFonhoverin, mousePos);
                  }),
              onExit: (PointerExitEvent event) =>
                  setState(() {
                    Offset point = event.position;

                    mousePos = [point.dx.toInt(), point.dy.toInt()];
                    processEvent(Node.MODIFonhoverout, mousePos);
                  })*/
//              onExit: (_) => appContainer.style.cursor = 'default',
          )
      );
      Widget textField = process.inputValues != null ? Transform.translate(
        offset: Offset(process.inputPos[0].toDouble(), process.inputPos[1].toDouble()),
        child:
        QEDTextField(
          onChanged: (String value) {
            process.inputValues[process.inputIndex] = value;
          },
          controller: _controller,
          qedState: this,
          onSubmitted: (String value) {
            process.inputValues[process.inputIndex] = value;

            if (process.onSubmitText != null) {
              process.onSubmitText();
              process.refresh();
            }
            else
              process.inputValues = null;
//                      _onSubmit(value);
//                      _controller.text = "";
          },
        ),
      ) : null;

      if (textField != null) {
        _controller.text = process.inputValues[process.inputIndex];
      }

      return textField != null ? Stack(children: [mainCanvas, textField]) : mainCanvas;//        );
    } else {
      return Container(
          color: Colors.white,
          child: Center(
              child: AspectRatio(
                aspectRatio: 0.4,
                child: Image.asset('images/qedLogo.png'),
              )
          )
      );
    }/*
    Rendering(
      startTime: Duration(seconds: 30),
      onTick: _simulateParticles,
      builder: (context, time) {
        return CustomPaint(
          painter: QEDPainter(particles, time),
        );
      },
    );
    // in painter
    ByteData data = image.toByteData();
    canvas.drawImage(image, Offset(0.0, 0.0), paint);
*/
  }

  void repaint() {
    setState(() {});
  }

  void setAppLoaded(QEDProcess process) {
    setState(() {
      this.process = process;
    });
  }

  _simulateParticles(Duration time) {
  }
}

class QEDPainter extends CustomPainter {
  DartQEDProcess process;

  QEDPainter(this.process);

  @override
  void paint(Canvas canvas, Size size) {
//    final paint = Paint()..color = Colors.white.withAlpha(50);
    process.canvas = canvas;
    process.paint = Paint();
    process.redraw();
    process.canvas = null;
    process.paint = null;
  }

  @override
  bool shouldRepaint(CustomPainter oldDelegate) => true;
}
/*
class ParticleModel {
  Animatable tween;
  double size;

  ParticleModel() {
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;

  void _incrementCounter() {
    setState(() {
      // This call to setState tells the Flutter framework that something has
      // changed in this State, which causes it to rerun the build method below
      // so that the display can reflect the updated values. If we changed
      // _counter without calling setState(), then the build method would not be
      // called again, and so nothing would appear to happen.
      _counter++;
    });
  }

  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
      appBar: AppBar(
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: Text(widget.title),
      ),
      body: Center(
        // Center is a layout widget. It takes a single child and positions it
        // in the middle of the parent.
        child: Column(
          // Column is also a layout widget. It takes a list of children and
          // arranges them vertically. By default, it sizes itself to fit its
          // children horizontally, and tries to be as tall as its parent.
          //
          // Invoke "debug painting" (press "p" in the console, choose the
          // "Toggle Debug Paint" action from the Flutter Inspector in Android
          // Studio, or the "Toggle Debug Paint" command in Visual Studio Code)
          // to see the wireframe for each widget.
          //
          // Column has various properties to control how it sizes itself and
          // how it positions its children. Here we use mainAxisAlignment to
          // center the children vertically; the main axis here is the vertical
          // axis because Columns are vertical (the cross axis would be
          // horizontal).
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              'You have pushed the button this many times:',
            ),
            Text(
              '$_counter',
              style: Theme.of(context).textTheme.headline4,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: Icon(Icons.add),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}*/

class DartProcessReturnHandler extends ProcessReturnHandler {
  QEDState canvasState;

  DartProcessReturnHandler(this.canvasState);

  void onPause(QEDProcess process, Obj obj) {
//				print("Paused...");
//    canvasState.setAppLoaded(process);
  }

  void onHalt(QEDProcess process, Obj obj) {
    print("Halted...");
//    canvasState.setAppLoaded(process);
  }

  void onReturn(QEDProcess process, Obj obj, Object value) {
    print("Returning " + value.toString());
    super.onReturn(process, obj, value);
  }
}

class DartQEDProcess extends QEDProcess {
  QEDState state;
  Canvas canvas;
  Paint paint;

  DartQEDProcess(this.state);

  bool parseQED(String fileName, ProcessReturnHandler returnHandler) {
    parseQEDAsync(fileName, returnHandler);
    return true;
  }

  Future<Uint8List> parseQEDAsync(String fileName, ProcessReturnHandler returnHandler) async {
    try {
      var response = state.code;
//      await http.readBytes("https://qed-lang.org/" + fileName);

      processStream(new DartFakeStream(Int8List.fromList(response)), returnHandler);
      state.setAppLoaded(this);
      return response;
    } catch (e) {
      print("Error: " + e.toString());
      return null;
    }
  }

  void initNow() {
  }

  void init2(List<int> size) {
  }

  void uninit() {
  }

  int getavgcharwidth(int fontSize) {
    return 8;
  }

  int getavgcharheight(int fontSize) {
    return 16;
  }

  // Here it is!
  static Size _textSize(String text, int fontSize, TextStyle style) {
    final TextPainter textPainter = TextPainter(
        text: TextSpan(
            style:style,
            children: <TextSpan>[
              TextSpan(
                  text: text,
                  style: fontSize >= 0 ? TextStyle(fontSize: fontSize.toDouble()) : TextStyle()
              )
            ]
        ),
        maxLines: 1,
        textDirection: TextDirection.ltr)
      ..layout(minWidth: 0, maxWidth: double.infinity);

    return textPainter.size;
  }

  double gettextsize(String value, int fontSize) {
    return _textSize(value, fontSize, DefaultTextStyle.of(state.widget.context).style).width;
  }

  double gettextheight(String value, int fontSize) {
    return _textSize(value, fontSize, DefaultTextStyle.of(state.widget.context).style).height;
  }

  List<int> getscreenarea() {
    return [10000, 10000];
  }

  bool resize(List<int> size) {/*
    Dimension dim = getSize();
    Node.log3("Resize called.");
    setSize(Math.max((int) dim.getWidth(), size[0] + 20), Math.max((int) dim.getHeight(), size[1] + 40));
    canvas.setSize(Math.max((int) dim.getWidth(), size[0] + 20), Math.max((int) dim.getHeight(), size[1] + 40));
    refresh(new int[2], size);
    //        	deviceform.getui().mainFrame.canvas.repaint(pos[0], pos[1], size[0], size[1]);
*/    return (true);
  }

  bool refreshNow(List<int> pos, List<int> size) {
    return false;
  }

  void repaintNow() {
    state.repaint();
  }

  Object createImageBuffer() {
    return null;
  }

  Object loadImage(String name, QEDProcess process, Obj obj) {
    return null;
  }

  int getImageWidth(Object image, int fontSize) {
    return 0;
  }

  int getImageHeight(Object image, int fontSize) {
    return 0;
  }

  Object setTimer(int delay, final QEDProcess process, final Obj obj) {
    const ms = const Duration(milliseconds: 1);

    return [Timer(ms * delay, () {
      obj.onReturn(process, null);
      process.tick();
    })];
  }

  void resetTimer(int delay, Object timerObjs) {
    const ms = const Duration(milliseconds: 1);
    final List<Object> objs = timerObjs;
    Timer timer = objs[0];

    timer.cancel();

    if (delay > 0) {
      objs[0] = timer = Timer(ms * delay, () {
        Obj obj = objs[1];
        QEDProcess process = objs[2];

        obj.onReturn(process, null);
        process.tick();
      });
    }
    else
      objs[0] = null;
  }

  void setclippingarea(List<int> pos, List<int> size) {
  }

  Paint color;
  int style = null;

  void setColor(int col) {
    color = Paint();
    color.color = Color(col != -1 ? col ^ 0xFF000000 : 0xFF000000);
  }

  void setStyle(int style) {
    this.style = style != -1 ? style : null;
  }

  void fillRectangle(List<int> pos, List<int> size) {
    Rect rect = Rect.fromLTWH(pos[0].toDouble(), pos[1].toDouble(), size[0].toDouble(), size[1].toDouble());
    Paint paint = new Paint();
    paint.color = color.color;
    paint.style = PaintingStyle.stroke;
    paint.strokeWidth = 1.0;

//    canvas.clip(new Rectangle2D.Float(pos[0], pos[1], size[0], size[1]));
    canvas.drawRect(rect, paint);
  }

  void fillRoundRectangle(List<int> pos, List<int> size, List<int> arc) {
  }

  void fillOval(List<int> pos, List<int> size) {
    Rect rect = Rect.fromLTWH(pos[0].toDouble(), pos[1].toDouble(), size[0].toDouble(), size[1].toDouble());
    double posx = pos[0].toDouble();
    double posy = pos[1].toDouble();
    double sizex = size[0].toDouble();
    double sizey = size[1].toDouble();
    double rx = sizex / 2;
    double ry = sizey / 2;

    canvas.clipRRect(RRect.fromLTRBAndCorners(posx, posy, posx + sizex, posy + sizey, topLeft: Radius.elliptical(rx, ry), topRight: Radius.elliptical(rx, ry), bottomLeft: Radius.elliptical(rx, ry), bottomRight: Radius.elliptical(rx, ry)));
    canvas.drawRect(rect, color);
  }

  void line(List<int> pos1, List<int> pos2) {
  }

  void lineWithStroke(List<int> pos1, List<int> pos2, int stroke) {
  }

  void drawText(String string, List<int> pos, List<int> size, List<int> unitpos, int fontSize, int textcol, int cursor, int sel) {
    double fontSizeDouble = fontSize >= 0 ? fontSize.toDouble() : null;
    TextDecoration textDecoration = (style != null && (style & 12) != 0) ? TextDecoration.combine([(style & 4) != 0 ? TextDecoration.underline : TextDecoration.none, (style & 8) != 0 ? TextDecoration.lineThrough : TextDecoration.none]) : null;
    FontWeight fontWeight = (style != null && (style & 1) != 0) ? FontWeight.bold : null;
    FontStyle fontStyle = (style != null && (style & 2) != 0) ? FontStyle.italic : null;
    final ui.ParagraphBuilder paragraphBuilder = ui.ParagraphBuilder(
        ui.ParagraphStyle()//textDirection: ui.TextDirection.ltr)
    )
      ..pushStyle(ui.TextStyle(color: ui.Color(textcol != -1 ? 0xFF000000 ^ textcol : 0xFF000000),
          fontSize: fontSizeDouble,
          fontStyle: fontStyle,
          fontWeight: fontWeight,
          decoration: textDecoration)
      )
      ..addText(string);
    final ui.Paragraph paragraph = paragraphBuilder.build()
      ..layout(ui.ParagraphConstraints(width: size[0].toDouble()));
    canvas.drawParagraph(paragraph, Offset(unitpos[0].toDouble(), unitpos[1].toDouble()));
  }

  void drawImage(Object image, List<int> pos, List<int> unitpos) {
  }

  String globalStr = '';
  void printToConsole(String string) {
    globalStr += string;
  }

  void printlnToConsole(String string) {
    print(globalStr + string);
    globalStr = '';
  }

  void saveContext() {
  }

  void restoreContext() {
  }

  void exit(int value) {
    exitSystem(value);
  }
/*
	static void main(String argv[]) {
		File file = new File(argv[0]);

		argv[0] = file.getAbsolutePath();
		bool rc = new AWTQEDProcess().run(argv, new ProcessReturnHandler() {
			void onPause(QEDProcess process, Obj obj) {
//				print("Paused...");
			}

			void onHalt(QEDProcess process, Obj obj) {
				print("Halted...");
			}

			void onReturn(QEDProcess process, Obj obj, Object value) {
				print("Returning " + value);
				super.onReturn(process, call, value);
			}
		});

		// wait forever if it gets there
		while(rc)
			synchronized(argv) {
				try {
					argv.wait();
				} catch (InterruptedException e) {
				}
			}
	}
*/
}

class DartFakeStream extends FakeStream {
  int cursor = 0;
  Int8List bytes;

  DartFakeStream(Int8List bytes) {
    this.bytes = bytes;
  }

  int read() {
    return bytes[cursor++];
  }

  void readBytes(ByteData data) {
    var length = data.lengthInBytes;

    data.buffer.asUint8List(0, length).setRange(0, length, bytes.getRange(cursor, cursor + length));
    cursor += length;
  }
}
