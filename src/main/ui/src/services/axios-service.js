import axios from "axios";
import * as constants from "../constants/page-constants";
import {usersDoneLoading} from "../actions/user-actions";

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

  return axios.post(constants.CREATE_USER + '?token=' + user.token, newUser);
};

export const getReadingsCSV = (user) => {

  return axios.get(constants.CSV_EXPORT + '?userId=' + user.token + "&table=readings");
};

export const getChart = (user, type, startDate, endDate) => {

  const chartRequest = {
    fromDate: startDate,
    toDate: endDate,
    type: type
  }

  return axios.post(constants.DATA_VISUAL + '?userId=' + user.token, chartRequest);
}