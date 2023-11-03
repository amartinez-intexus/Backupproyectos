import 'dart:developer';
import 'package:app_test/api_Screen.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:camera/camera.dart';
import 'package:local_auth/local_auth.dart';
import 'package:nfc_manager/nfc_manager.dart';
import 'android_channel.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        useMaterial3: true,
        primarySwatch: Colors.blue,
      ),
      home: HomeScreen(),
    );
  }
}

class HomeScreen extends StatelessWidget {
  final List<CameraDescription> cameras = [];
  final LocalAuthentication localAuth = LocalAuthentication();

  HomeScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leadingWidth: 90,
        leading: Image.asset('resources/Isotipo_Intexus.png'),
        title: const Text('Acceso a Funcionalidades'),
        centerTitle: true,
      ),
      body: Center(
          child: Column(
        mainAxisAlignment: MainAxisAlignment.start,
        children: [
          Image.asset('resources/INTEXUS LOGO VERDE.png'),
          Padding(
            padding: const EdgeInsets.all(20),
            child: SizedBox(
              width: 200.0,
              height: 50.0,
              child: ElevatedButton(
                onPressed: () {
                  _openCamera(context);
                },
                child: const Row(
                  children: <Widget>[
                    Icon(Icons.camera),
                    SizedBox(width: 20),
                    Text("Camera"),
                  ],
                ),
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(20),
            child: SizedBox(
              width: 200.0,
              height: 50.0,
              child: ElevatedButton(
                onPressed: () {
                  _authenticate();
                },
                child: const Row(
                  children: <Widget>[
                    Icon(Icons.fingerprint),
                    SizedBox(width: 20),
                    Text("Biometrics"),
                  ],
                ),
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(20),
            child: SizedBox(
              width: 200.0,
              height: 50.0,
              child: ElevatedButton(
                onPressed: () {
                  _startNFCDetection(context);
                },
                child: const Row(
                  children: <Widget>[
                    Icon(Icons.nfc),
                    SizedBox(width: 20),
                    Text("NFC"),
                  ],
                ),
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(20),
            child: SizedBox(
              width: 200.0,
              height: 50.0,
              child: ElevatedButton(
                onPressed: () {
                  Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (context) => const api_Screen()));
                },
                child: const Row(
                  children: <Widget>[
                    Icon(Icons.contact_page),
                    SizedBox(width: 20),
                    Text("Consumo API"),
                  ],
                ),
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(20),
            child: SizedBox(
              width: 200.0,
              height: 50.0,
              child: ElevatedButton(
                onPressed: () async {
                  showSumResultDialog(context);
                },
                child: const Row(
                  children: <Widget>[
                    Icon(Icons.android),
                    SizedBox(width: 20),
                    Text("Android Test"),
                  ],
                ),
              ),
            ),
          ),
        ],)
      ),
    );
  }
}

Future<void> _authenticate() async {
  final LocalAuthentication auth = LocalAuthentication();

  try {
    bool authenticated = await auth.authenticate(
      localizedReason: 'Test for biometrics',
      options: const AuthenticationOptions(
        stickyAuth: true,
        biometricOnly: false,
      ),
    );

    log("Authenticated: $authenticated");
  } on PlatformException catch (e) {
    log('$e');
  }
}

void _openCamera(BuildContext context) async {
  final cameras = await availableCameras();
  if (cameras.isNotEmpty) {
    final camera = cameras.first;
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => CameraScreen(camera: camera),
      ),
    );
  } else {
    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(
        content: Text('No se encontraron cámaras disponibles.'),
      ),
    );
  }
}

final NfcManager nfcManager = NfcManager.instance;

void _startNFCDetection(context) {
  try {
    nfcManager.startSession(
      onDiscovered: (NfcTag tag) async {
        log('NFC Tag ID: ${tag.data}');
        showDialog(
          context: context,
          builder: (BuildContext context) {
            return AlertDialog(
              title: const Text('NFC Card Read'),
              content: Text('NFC Tag ID: ${tag.data}'),
              actions: <Widget>[
                TextButton(
                  child: const Text('OK'),
                  onPressed: () {
                    Navigator.of(context).pop();
                  },
                ),
              ],
            );
          },
        );
      },
    );
  } catch (e) {
    log('$e');
  }
}

class CameraScreen extends StatefulWidget {
  final CameraDescription camera;

  const CameraScreen({super.key, required this.camera});

  @override
  _CameraScreenState createState() => _CameraScreenState();
}

class _CameraScreenState extends State<CameraScreen> {
  late CameraController _controller;

  @override
  void initState() {
    super.initState();
    _controller = CameraController(widget.camera, ResolutionPreset.medium);

    _controller.initialize().then((_) {
      if (!mounted) {
        return;
      }
      setState(() {});
    });
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    if (!_controller.value.isInitialized) {
      return Container();
    }
    return Scaffold(
      appBar: AppBar(
        title: const Center(child: Text('Camera')),
      ),
      body: Center(
        child: CameraPreview(_controller),
      ),
    );
  }
}

void showSumResultDialog(BuildContext context) async {
  ComuncationAndroid comuncationAndroid = ComuncationAndroid();
  int result = await comuncationAndroid.sumatoryNumbers(123, 321); // You might need to adjust this method name to match your implementation

  showDialog(
    context: context, 
    builder: (BuildContext context) {
      return AlertDialog(
        title: const Text('Sum Result'),
        content: Text('Result of sumatory: $result'),
        actions: <Widget>[
          TextButton(
            onPressed: () {
              Navigator.of(context).pop();
            },
            child: const Text('OK'),
          ),
        ],
      );
    },
  );
}
