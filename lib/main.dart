import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const chanel = MethodChannel('batterylevel');
  String _batterieLevel = 'Unknown battery level.';

  Future<void> _getBatteriyLevel() async {
    String batteryLevel;
    try {
      final int result = await chanel.invokeMethod('getBatteryLevel');
      batteryLevel = 'Battery level at $result%';
    } on PlatformException catch (e) {
      batteryLevel = 'Failed to get battery level: ${e.message}';
    }

    setState(() {
      _batterieLevel = batteryLevel;
    });
  }

  Future<void> Toast(String message) async {
    await chanel.invokeMethod('toast', message);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: <Widget>[
          RaisedButton(
            child: Text('Get battery level'),
            onPressed: _getBatteriyLevel,
          ),
          RaisedButton(
            child: Text('Toast'),
            onPressed: () =>
                Toast('Bonjour la plateforme specifique code est magique'),
          ),
          Text(_batterieLevel)
        ],
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
