import axios from "axios";
import * as constants from "../constants/page-constants";

/*const getEndpoint = endpoint => {
  return process.env.NODE_ENV === "development" ||
    process.env.NODE_ENV === "test"
    ? `http://192.168.1.25:7733${endpoint}`
    : endpoint;
};*/

export const load = () => axios.get(constants.LOADING_PAGE);

export const loadSummary = (user) => {
  const params = {
    userId: user.token
  }
  return axios.get(constants.SUMMARY_PAGE + '?userId=' + user.token);
} 

export const logOn = (username, password) => {
  const LogonForm = {
    username: username,
    password: password
  };

  return axios.post(constants.LOGIN_PAGE, LogonForm);
};

export const logOut = () => {
  return axios.get(constants.LOGOUT);
}
