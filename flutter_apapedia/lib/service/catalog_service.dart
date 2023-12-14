import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:flutter_apapedia/models/catalog.dart';
import 'dart:typed_data';
import 'package:shared_preferences/shared_preferences.dart';


class CatalogService {
  final String baseUrl;

  CatalogService({required this.baseUrl});

  Future<List<Result>> getAllCatalogs() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? token = prefs.getString('token');    
    final response = await http.get(
      Uri.parse('$baseUrl/catalog/all-catalogs'),
        headers: {
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Headers':
              'Origin, X-Requested-With, Content-Type, Accept',
          'Authorization': 'Bearer $token',
        },      
      );

    if (response.statusCode == 200) {
      final List<dynamic> data = json.decode(response.body)['result'];
      return data.map((model) => Result.fromJson(model)).toList();
    } else {
      throw Exception('Failed to load catalogs');
    }
  }

  // Future<Uint8List> getImage(String catalogId) async {
  //   final response =
  //       await http.get(Uri.parse('$baseUrl/api/catalog/image/$catalogId'));

  //   if (response.statusCode == 200) {
  //     return Uint8List.fromList(response.bodyBytes);
  //   } else {
  //     throw Exception('Failed to load image');
  //   }
  // }

  Future<Result> getCatalogById(String catalogId) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? token = prefs.getString('token');     
    final response =
        await http.get(
          Uri.parse('$baseUrl/api/catalog/$catalogId'),
            headers: {
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*',
              'Access-Control-Allow-Headers':
                  'Origin, X-Requested-With, Content-Type, Accept',
              'Authorization': 'Bearer $token',
            },           
          );

    if (response.statusCode == 200) {
      final Map<String, dynamic> data = json.decode(response.body)['result'];
      return Result.fromJson(data);
    } else {
      throw Exception('Failed to load catalog by ID');
    }
  }
}