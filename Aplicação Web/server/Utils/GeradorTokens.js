const jwt = require("jsonwebtoken");
const jwtToken = process.env.JWT_TOKEN;

function jwtGenerator(user_id) {
  const payload = {
    user: {
      id: user_id
    }
  };
  
  return jwt.sign(payload, jwtToken, { expiresIn: 60 * 60 });
}

module.exports = jwtGenerator;