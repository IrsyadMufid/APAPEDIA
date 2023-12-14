import 'dart:typed_data';


import 'package:flutter/material.dart';
import 'package:flutter_apapedia/models/Category.dart';
import 'package:flutter_apapedia/models/Product.dart';
import 'package:flutter_apapedia/screens/catalog/ProductDetailPage.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;

class CatalogPage extends StatefulWidget {
  const CatalogPage({super.key});

  @override
  _CatalogPageState createState() => _CatalogPageState();
}

class _CatalogPageState extends State<CatalogPage> {
  late List<Product> products;
  late List<Product> filteredProducts;

  TextEditingController searchController = TextEditingController();
  TextEditingController minPriceController = TextEditingController();
  TextEditingController maxPriceController = TextEditingController();

  String selectedSortBy = 'productName'; // Default sort by product name
  String selectedSortOrder = 'asc'; // Default sort order ascending
  RangeValues priceRange = const RangeValues(0, 1000000000);

  @override
  void initState() {
    super.initState();
    fetchProducts();
    fetchCategories();
  }

  Future<void> fetchProducts() async {
    final response = await http.get(Uri.parse('http://localhost:8083/catalog/all-catalogs-sort'));

    if (response.statusCode == 200) {
      final List<dynamic> jsonData = json.decode(response.body);
      setState(() {
        products = jsonData
            .map((data) => Product.fromJson(data))
            .where((product) => product.isDeleted != true)
            .toList();

        // Initially set filteredProducts to all products
        filteredProducts = List.from(products);
      });
    } else {
      throw Exception('Failed to load products');
    }
  }

  Future<void> fetchFilteredProducts(String endpoint, Map<String, dynamic> params) async {
    // Construct URL with query parameters
    final Uri uri = Uri.parse('http://localhost:8080/catalog/$endpoint')
        .replace(queryParameters: params.map((key, value) => MapEntry(key, value.toString())));

    // Make the HTTP request
    final response = await http.get(uri);

    if (response.statusCode == 200) {
      final List<dynamic> jsonData = json.decode(response.body);
      setState(() {
        filteredProducts = jsonData
            .map((data) => Product.fromJson(data))
            .where((product) => product.isDeleted != true)
            .toList();
      });
    } else {
      throw Exception('Failed to load filtered products');
    }
  }

  void applySearchFilter() async {
    String searchTerm = searchController.text.toLowerCase();
    await fetchFilteredProducts('by-name', {'productName': searchTerm});
    setState(() {});
  }

  void applyPriceFilter() async {
    double minPrice = double.tryParse(minPriceController.text) ?? 0;
    double maxPrice = double.tryParse(maxPriceController.text) ?? 1000000000;
    priceRange = RangeValues(minPrice, maxPrice);
    await fetchFilteredProducts('by-price', {'minPrice': minPrice.toString(), 'maxPrice': maxPrice.toString()});
    setState(() {});
  }

  void applySort() async {
    await fetchFilteredProducts('sorted', {'sortBy': selectedSortBy, 'sortOrder': selectedSortOrder});
    setState(() {});
  }

  // Add a new method to fetch categories
Future<void> fetchCategories() async {
  final response = await http.get(Uri.parse('http://localhost:8083/category/all'));

  if (response.statusCode == 200) {
    final List<dynamic> jsonData = json.decode(response.body);
    setState(() {
      // Assuming Category class structure similar to Product
      List<Category> categories = jsonData
          .map((data) => Category.fromJson(data))
          .toList();

      // Do something with the categories (e.g., store in state)
      // Example: this.categories = categories;
    });
  } else {
    throw Exception('Failed to load categories');
  }
}

// Call fetchCategories in initState or wherever appropriate


// Update UI to display categories
// ...



  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('APAPEDIA Catalog'),
      ),
      body: Column(
        children: [
          // Search TextField
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: TextField(
              controller: searchController,
              decoration: const InputDecoration(
                labelText: 'Search',
                prefixIcon: Icon(Icons.search),
                border: OutlineInputBorder(),
              ),
            ),
          ),
          // Price Range TextFields
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Row(
              children: [
                Expanded(
                  child: TextFormField(
                    controller: minPriceController,
                    keyboardType: TextInputType.number,
                    decoration: const InputDecoration(
                      labelText: 'Min Price',
                      border: OutlineInputBorder(),
                    ),
                  ),
                ),
                const SizedBox(width: 8.0),
                Expanded(
                  child: TextFormField(
                    controller: maxPriceController,
                    keyboardType: TextInputType.number,
                    decoration: const InputDecoration(
                      labelText: 'Max Price',
                      border: OutlineInputBorder(),
                    ),
                  ),
                ),
              ],
            ),
          ),
          // Sort Dropdown
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              const Text('Sort By: '),
              DropdownButton<String>(
                value: selectedSortBy,
                onChanged: (value) {
                  setState(() {
                    selectedSortBy = value!;
                  });
                },
                items: ['productName', 'price']
                    .map((sortBy) => DropdownMenuItem<String>(
                  value: sortBy,
                  child: Text(sortBy),
                ))
                    .toList(),
              ),
              DropdownButton<String>(
                value: selectedSortOrder,
                onChanged: (value) {
                  setState(() {
                    selectedSortOrder = value!;
                  });
                },
                items: ['asc', 'desc']
                    .map((sortOrder) => DropdownMenuItem<String>(
                  value: sortOrder,
                  child: Text(sortOrder.toUpperCase()),
                ))
                    .toList(),
              ),
            ],
          ),
          // Apply Filter Button
ElevatedButton(
            onPressed: () {
              applySearchFilter();
            },
            child: const Text('Search'),
          ),

          // Price Filter Button
          ElevatedButton(
            onPressed: () {
              applyPriceFilter();
            },
            child: const Text('Filter by Price'),
          ),

          // Sort Button
          ElevatedButton(
            onPressed: () {
              applySort();
            },
            child: const Text('Sort'),
          ),

          // Product List
          Expanded(
            child: filteredProducts != null
                ? GridView.builder(
                    gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 2,
                      crossAxisSpacing: 8.0,
                      mainAxisSpacing: 8.0,
                    ),
                    itemCount: filteredProducts.length,
                    itemBuilder: (context, index) {
                      return GestureDetector(
                        onTap: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => ProductDetailPage(product: filteredProducts[index]),
                            ),
                          );
                        },
                        child: Card(
                          elevation: 4.0,
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(8.0),
                          ),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                                Expanded(
  child: ClipRRect(
    borderRadius: const BorderRadius.vertical(
      top: Radius.circular(8.0),
    ),
    child: (() {
      String? imageBase64 = filteredProducts[index].imageBase64;
      print('Image Base64: $imageBase64');
      if (imageBase64 != null && imageBase64.isNotEmpty) {
        try {
          return Image.memory(
            const Base64Decoder().convert(imageBase64),
            fit: BoxFit.cover,
          );
        } catch (e) {
          // Handle the ImageCodecException
          print('Error loading image: $e');
          return Container(
            color: Colors.grey, // Use a placeholder color or any other fallback UI
          );
        }
      }
      // Return a placeholder widget or any fallback UI if image data is not available
      return Placeholder();
    })(),
  ),
),
                              Padding(
                                padding: const EdgeInsets.all(8.0),
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Text(
                                      filteredProducts[index].productName ?? '',
                                      style: const TextStyle(
                                        fontWeight: FontWeight.bold,
                                      ),
                                    ),
                                    const SizedBox(height: 4.0),
                                    Text(
                                      filteredProducts[index].productDescription ?? '',
                                      maxLines: 2,
                                      overflow: TextOverflow.ellipsis,
                                    ),
                                    const SizedBox(height: 4.0),
                                    Text(
                                      'Price: \$${filteredProducts[index].price ?? 0}',
                                      style: const TextStyle(
                                        color: Colors.green,
                                        fontWeight: FontWeight.bold,
                                      ),
                                    ),
                                    const SizedBox(height: 8.0),
                                    Row(
                                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                                      children: [
                                        ElevatedButton(
                                          onPressed: () {
                                            // Handle Add To Cart button click
                                            // Placeholder: Implement Add To Cart functionality
                                            print('Add To Cart clicked for ${filteredProducts[index].productName}');
                                          },
                                          style: ElevatedButton.styleFrom(
                                            backgroundColor: Colors.orange,
                                          ),
                                          child: const Text('Add To Cart'),
                                        ),
                                        ElevatedButton(
                                          onPressed: () {
                                            // Handle Buy Now button click
                                            // Placeholder: Implement Buy Now functionality
                                            print('Buy Now clicked for ${filteredProducts[index].productName}');
                                          },
                                          style: ElevatedButton.styleFrom(
                                            primary: Colors.blue,
                                          ),
                                          child: const Text('Buy Now'),
                                        ),
                                      ],
                                    ),
                                  ],
                                ),
                              ),
                            ],
                          ),
                        ),
                      );
                    },
                  )
                : const Center(
                    child: CircularProgressIndicator(),
                  ),
          ),
        ],
      ),
    );
  }
}