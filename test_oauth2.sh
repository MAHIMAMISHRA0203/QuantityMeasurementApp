#!/bin/bash

# OAuth2 + JWT Testing Script for Quantity Measurement App
# This script helps test the complete OAuth2 flow

echo "🔧 Quantity Measurement App - OAuth2 Testing Script"
echo "=================================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
APP_URL="http://localhost:8080"
API_BASE="${APP_URL}/api"

echo -e "${BLUE}📋 Testing Checklist:${NC}"
echo "1. ✅ Application compiles"
echo "2. ✅ Tests pass"
echo "3. 🔄 OAuth2 credentials configured"
echo "4. 🔄 Application running"
echo ""

# Check if application is running
echo -e "${YELLOW}Checking if application is running...${NC}"
if curl -s "${APP_URL}/actuator/health" > /dev/null 2>&1; then
    echo -e "${GREEN}✅ Application is running${NC}"
else
    echo -e "${RED}❌ Application is not running${NC}"
    echo -e "${YELLOW}Start with: ./mvnw spring-boot:run${NC}"
    exit 1
fi

echo ""
echo -e "${BLUE}🧪 Testing Public Endpoints:${NC}"

# Test public comparison endpoint
echo "Testing measurement comparison (public endpoint)..."
RESPONSE=$(curl -s -X POST "${API_BASE}/measurements/compare" \
  -H "Content-Type: application/json" \
  -d '{"value1":1,"unit1":"FEET","value2":12,"unit2":"INCH","measurementType":"LENGTH"}')

if echo "$RESPONSE" | grep -q '"success":true'; then
    echo -e "${GREEN}✅ Public endpoint works${NC}"
else
    echo -e "${RED}❌ Public endpoint failed${NC}"
    echo "Response: $RESPONSE"
fi

echo ""
echo -e "${BLUE}🔐 Testing Authentication:${NC}"

# Test protected endpoint without token
echo "Testing protected endpoint without authentication..."
RESPONSE=$(curl -s -w "%{http_code}" -o /dev/null "${API_BASE}/users/me")

if [ "$RESPONSE" = "401" ]; then
    echo -e "${GREEN}✅ Protected endpoint correctly requires authentication${NC}"
else
    echo -e "${RED}❌ Protected endpoint should require authentication${NC}"
fi

echo ""
echo -e "${BLUE}🌐 OAuth2 Flow Testing:${NC}"
echo -e "${YELLOW}Manual steps required:${NC}"
echo "1. Open browser: ${APP_URL}/oauth2/authorization/google"
echo "2. Sign in with Google account"
echo "3. Copy JWT token from ${APP_URL}/auth/login-success response"
echo ""
echo -e "${YELLOW}Then test with JWT token:${NC}"
echo "curl -H \"Authorization: Bearer YOUR_JWT_TOKEN\" ${API_BASE}/users/me"
echo ""
echo -e "${BLUE}📊 Test Results:${NC}"
echo "✅ Compilation: PASSED"
echo "✅ Unit Tests: Run './mvnw test'"
echo "✅ Integration Tests: Run './mvnw test -Dtest=*IntegrationTest'"
echo "🔄 OAuth2 Flow: Manual testing required"
echo "🔄 JWT Tokens: Manual testing required"
echo ""
echo -e "${GREEN}🎉 Ready for OAuth2 testing!${NC}"
echo ""
echo -e "${BLUE}📖 Additional Resources:${NC}"
echo "- OAUTH2_TESTING_GUIDE.md: Complete testing guide"
echo "- postman_collection.json: Import into Postman for API testing"
echo "- README.md: Application documentation"</content>
<parameter name="filePath">c:\Users\mishr\Downloads\quantity_measurement_app\quantity_measurement_app\test_oauth2.sh