import 'package:flutter/material.dart';
import 'package:flutter_apapedia/screens/edit_profile.dart';
import 'package:flutter_apapedia/screens/apapay.dart';

class ProfilePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white, // Light background color for the whole page
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.end,
            children: [
              const SizedBox(height: 24),
              Center(
                child: Column(
                  children: [
                    const SizedBox(height: 16),
                    const Text(
                      'Dina Dwi',
                      style: TextStyle(color: Colors.black, fontSize: 24, fontWeight: FontWeight.bold),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      'dina.dwi11@ui.ac.id',
                      style: TextStyle(color: Colors.black54, fontSize: 16),
                    ),
                    const SizedBox(height: 4),
                    Text(
                      'Bandung',
                      style: TextStyle(color: Colors.black54, fontSize: 16),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 24),
              _buildCard(
                context,
                title: 'APAPAY Balance',
                content: 'Rp 100.000',
                icon: Icons.account_balance_wallet,
                actionWidget: ElevatedButton(
                  onPressed: () {
                    Navigator.of(context).push(
                      MaterialPageRoute(builder: (context) => TopUpPage()),
                    );
                  },
                  child: const Text('Top-up'),
                  style: ElevatedButton.styleFrom(primary: Colors.blue, shape: StadiumBorder()),
                ),
              ),
              const SizedBox(height: 24),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  _buildActionButton(
                    context,
                    text: 'Edit Profile',
                    icon: Icons.edit,
                    color: Colors.blue,
                    onTap: () {
                      Navigator.of(context).push(
                        MaterialPageRoute(builder: (context) => EditProfilePage()),
                      );
                    },
                  ),
                  _buildActionButton(
                    context,
                    text: 'Sign Out',
                    icon: Icons.logout,
                    color: Colors.grey,
                    onTap: () {
                      // Sign out action
                    },
                  ),
                  _buildActionButton(
                    context,
                    text: 'Delete Account',
                    icon: Icons.delete_forever,
                    color: Colors.redAccent,
                    onTap: () {
                      // Delete account action
                    },
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildCard(BuildContext context, {required String title, required String content, required IconData icon, required Widget actionWidget}) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.grey[200],
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.grey.withOpacity(0.5),
            spreadRadius: 0,
            blurRadius: 4,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(title, style: TextStyle(color: Colors.black54, fontSize: 18)),
          const SizedBox(height: 10),
          Row(
            children: [
              Icon(icon, color: Colors.blue),
              const SizedBox(width: 8),
              Text(content, style: TextStyle(color: Colors.black, fontSize: 30, fontWeight: FontWeight.bold)),
            ],
          ),
          const SizedBox(height: 16),
          Center(child: actionWidget),
        ],
      ),
    );
  }

  Widget _buildActionButton(BuildContext context, {required String text, required IconData icon, required Color color, required VoidCallback onTap}) {
    return ElevatedButton.icon(
      onPressed: onTap,
      icon: Icon(icon, size: 24),
      label: Text(text),
      style: ElevatedButton.styleFrom(
        primary: color,
        onPrimary: Colors.white,
        minimumSize: Size(100, 40),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(10),
        ),
      ),
    );
  }
}
