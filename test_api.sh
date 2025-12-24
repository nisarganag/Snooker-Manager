#!/bin/bash

BASE_URL="http://localhost:8080/api"
# Shortened username to fit within 20 chars (adm_1234567890)
USERNAME="adm_$(date +%s)"
EMAIL="ad$(date +%s)@test.com"
PASSWORD="password123"

echo "--------------------------------------------------"
echo "1. Registering User ($USERNAME)..."
# Using -v to debug if needed, but capturing output
curl -s -X POST "$BASE_URL/auth/signup" \
  -H "Content-Type: application/json" \
  -d "{
    \"username\": \"$USERNAME\",
    \"email\": \"$EMAIL\",
    \"password\": \"$PASSWORD\",
    \"role\": [\"admin\"]
  }" > register_output.txt

cat register_output.txt
echo ""

echo -e "\n--------------------------------------------------"
echo "2. Signing In..."
LOGIN_OUTPUT=$(curl -s -X POST "$BASE_URL/auth/signin" \
  -H "Content-Type: application/json" \
  -d "{
    \"username\": \"$USERNAME\",
    \"password\": \"$PASSWORD\"
  }")
echo "$LOGIN_OUTPUT"

# Extract Token using sed for robustness
TOKEN=$(echo "$LOGIN_OUTPUT" | sed -n 's/.*"token":"\([^"]*\)".*/\1/p')

if [ -z "$TOKEN" ]; then
  echo "Error: Could not retrieve token from: $LOGIN_OUTPUT"
  exit 1
fi

echo "Token retrieved: ${TOKEN:0:10}..."

echo -e "\n--------------------------------------------------"
echo "3. Creating Player..."
# Removed -s to see errors
curl -v -X POST "$BASE_URL/players/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Ronnie O'\''Sullivan",
    "nationality": "British"
  }' > create_player_response.txt 2>&1

cat create_player_response.txt
echo ""

echo -e "\n--------------------------------------------------"
echo "4. Getting All Players..."
curl -v -X POST "$BASE_URL/players/all" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" > get_players_response.txt 2>&1

cat get_players_response.txt
echo ""
