// import "./envConfig.js";

export default {
    API_URL: "http://localhost:8080",
    GET_USER_CREDENTIALS: function (localStorage) {
        const userDetailStr = localStorage.getItem("user_detail");
        if (userDetailStr) {
            try {
                return JSON.parse(userDetailStr);
            } catch (error) {
                console.error("Error parsing user detail from localStorage:", error);
            }
        }
    },
};
