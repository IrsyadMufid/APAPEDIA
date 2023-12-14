import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:flutter_apapedia/models/catalog.dart';
import 'package:flutter_apapedia/models/cart_response.dart';
import 'dart:typed_data';
import 'package:shared_preferences/shared_preferences.dart';


class OrderService {
  final String baseUrl;

  OrderService({required this.baseUrl});


  Future<Cart> getCartByUserId(String userId) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? token = prefs.getString('token');     
    final response = await http.get(
      Uri.parse('$baseUrl/api/cart/?userId=$userId'),
        headers: {
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Headers':
              'Origin, X-Requested-With, Content-Type, Accept',
          'Authorization': 'Bearer $token',
        },       
      );

    if(response.statusCode == 200) {
      final Map<String, dynamic> data = json.decode(response.body)['result'];
      return Cart.fromJson(data);
    } else {
      throw Exception('Failed to load Cart by User ID');

    }
  }

  Future<CartItem> updateCartItemQuantity(CartItem cartItem) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? token = prefs.getString('token');     
    final response = await http.put(
      Uri.parse('$baseUrl/api/cart/cart-item/update'),
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Headers':
            'Origin, X-Requested-With, Content-Type, Accept',
        'Authorization': 'Bearer $token',
      },  
      body: json.encode(cartItem.toJsonForUpdate()), // Assuming you have a method toJson() in your DTO
    );

    print(response.body);

    if(response.statusCode == 200) {
      final Map<String, dynamic> data = json.decode(response.body)['result'];
      return CartItem.fromJson(data);
    } else {
      throw Exception('Failed to update cart item quantity');

    }
  }

  

}