import 'package:flutter/material.dart';
import 'package:flutter_apapedia/models/cart_response.dart';
import 'package:flutter_apapedia/models/catalog.dart';
import 'package:flutter_apapedia/service/catalog_service.dart';
import 'package:flutter_apapedia/service/order_service.dart';
import 'package:flutter_apapedia/utils/drawer.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'dart:typed_data';

class CartItemsList extends StatefulWidget {
  final OrderService orderService;
  final CatalogService catalogService;

  CartItemsList({
    required this.orderService,
    required this.catalogService
    });

  @override
  _CartItemsListState createState() => _CartItemsListState();
}

// Import statement yang sesuai

class _CartItemsListState extends State<CartItemsList> {
  late Future<Cart> cart;
  late Future<List<Result>> catalogs;

  @override
  Future<void> initState() async {
    super.initState();
    final prefs = await SharedPreferences.getInstance();
    String? token = prefs.getString('token');
    String? activeUserId = prefs.getString('activeUserId');
    String id = activeUserId.toString();
    cart = widget.orderService.getCartByUserId(id); 
    catalogs = widget.catalogService.getAllCatalogs();
  }

  Future<CartItem> incrementCartItemQuantity(CartItem cartItem) async {
    try {
      cartItem.quantity = cartItem.quantity + 1;
      return await widget.orderService.updateCartItemQuantity(cartItem);
    } catch (e) {
      // Handle error
      throw Exception('Failed to update cart item quantity');
    }
  }

  Future<CartItem> decrementCartItemQuantity(CartItem cartItem) async {
    try {
      cartItem.quantity = cartItem.quantity - 1;
      return await widget.orderService.updateCartItemQuantity(cartItem);
    } catch (e) {
      // Handle error
      throw Exception('Failed to update cart item quantity');
    }
  }

    Future<void> updateQuantityAndTotalPrice(CartItem cartItem) async {
    try {
      // Update the quantity on the server
      await widget.orderService.updateCartItemQuantity(cartItem);

      // Trigger a rebuild of the widget to update UI
      setState(() {
        // Nothing specific needs to be done here since the UI will be updated in the build method
      });
    } catch (e) {
      // Handle error
      print('Failed to update cart item quantity: $e');
    }
  }

    Future<Result> getCatalogById(String catalogId) async {
    try {
      return await widget.catalogService.getCatalogById(catalogId);
    } catch (e) {
      // Handle error
      throw Exception('Failed to load catalog by ID');
    }
  }

@override
Widget build(BuildContext context) {
  return Scaffold(
    appBar: AppBar(
      title: Text('Cart Page'),
    ),
    drawer: CustomDrawer(),
    body: FutureBuilder<Cart>(
      future: cart,
      builder: (context, cartSnapshot) {
        if (cartSnapshot.connectionState == ConnectionState.waiting) {
          return Center(child: CircularProgressIndicator());
        } else if (cartSnapshot.hasError) {
          return Center(child: Text('Error: ${cartSnapshot.error}'));
        } else if (!cartSnapshot.hasData) {
          return Center(child: Text('No data available'));
        }

        var cart = cartSnapshot.data!;

        return ListView.builder(
          itemCount: cart.listCartItem.length,
          itemBuilder: (context, index) {
            var cartItem = cart.listCartItem[index];

            return FutureBuilder<Result>(
              future: getCatalogById(cartItem.productId),
              builder: (context, catalogSnapshot) {
                if (catalogSnapshot.connectionState == ConnectionState.waiting) {
                  return CircularProgressIndicator();
                } else if (catalogSnapshot.hasError) {
                  return Text('Error: ${catalogSnapshot.error}');
                } else if (!catalogSnapshot.hasData) {
                  return Text('No data available');
                }

                var catalog = catalogSnapshot.data!;

                return Card(
                  child: ListTile(
                    title: Text(catalog.productName),
                    subtitle: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text('Price: \$${catalog.price.toStringAsFixed(2)}'), // Assuming productPrice is a double
                        Row(
                          children: [
                            ElevatedButton(
                                onPressed: () async {
                                  cartItem.quantity = cartItem.quantity + 1;
                                  await updateQuantityAndTotalPrice(cartItem);
                                },
                                child: Icon(Icons.add),
                              ),
                            SizedBox(width: 8), // Adjust spacing as needed
                            Text('Quantity: ${cartItem.quantity}'),
                            SizedBox(width: 8),
                            ElevatedButton(
                                onPressed: () async {
                                  cartItem.quantity = cartItem.quantity - 1;
                                  await updateQuantityAndTotalPrice(cartItem);
                                },
                                child: Icon(Icons.remove),
                              ),
                          ],
                        ),
                        Text('Total Price: \$${(catalog.price * cartItem.quantity).toStringAsFixed(2)}'),
                      ],
                    ),
                    trailing: ElevatedButton(
                      onPressed: () {
                        // TODO: Handle delete
                      },
                      child: Icon(Icons.delete),
                    ),
                  ),
                );
              },
            );
          },
        );
      },
    ),
  );
}
}