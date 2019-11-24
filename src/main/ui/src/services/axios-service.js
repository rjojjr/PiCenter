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
} ;

export const logOn = (username, password) => {
  const LogonForm = {
    username: username,
    password: password
  };

  return axios.post(constants.LOGIN_PAGE, LogonForm);
};

export const logOut = () => {
  return axios.get(constants.LOGOUT);
};

export const updateSession = (currentPage, user) => {

  const SessionUpdate = {
    page: currentPage
  };

  axios.post(constants.UPDATE_SESSION + '?userId=' + user.token, SessionUpdate);
};

export const createUser = (user, username, firstName, lastName, password, admin) => {

  const newUser = {
    userName: username,
    firstName: firstName,
    lastName: lastName,
    password: password,
    admin: admin
  };

  axios.post(constants.CREATE_USER + '?token=' + user.token, newUser);
};