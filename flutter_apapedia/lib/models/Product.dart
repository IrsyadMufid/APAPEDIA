class Product {
  String? id;
  String? productName;
  String? productDescription;
  int? price;
  String? imageBase64;
  String? categoryName;
  bool? isDeleted;  // Added property for soft delete

  Product({
    required this.id,
    required this.productName,
    required this.productDescription,
    required this.price,
    required this.imageBase64,
    required this.categoryName,
    required this.isDeleted,
  });

  factory Product.fromJson(Map<String, dynamic> json) {
    return Product(
      id: json['id'] ?? '',
      productName: json['productName'] ?? '',
      productDescription: json['productDescription'] ?? '',
      price: json['price'] ?? 0,
      imageBase64: json['imageBase64'] ?? '',
      categoryName: json['category']['name'] ?? '',
      isDeleted: json['isDeleted'] ?? false,  // Assuming 'isDeleted' is a boolean property
    );
  }
}
