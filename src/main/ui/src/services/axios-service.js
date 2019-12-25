import axios from "axios";
import * as constants from "../constants/page-constants";
import {usersDoneLoading} from "../actions/user-actions";
import {dateStringFormat} from "./helper-service";
import {PI_STATUSES} from "../constants/page-constants";
import {RESTART_PITEMP} from "../constants/page-constants";
import {RESTART_DHT, RESTART_PI} from "../constants/page-constants";

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

export const getChart = (user, startDate, endDate, type, flavor) => {

  let endpoint;

  if (flavor === 'avg'){
    endpoint = constants.DATA_VISUAL;
  }else{
    endpoint = constants.DATA_VISUAL_DIFF;
  }

  const chartRequest = {
    fromDate: startDate,
    toDate: endDate,
    type: type
  }

  return axios.post(endpoint + '?userId=' + user.token, chartRequest);
}

export const getPiStatuses = (user) => {
  return axios.get(PI_STATUSES + '?userId=' + user.token);
}

export const restartPitemp = (user, pi) => {
  return axios.get(RESTART_PITEMP + '?userId=' + user.token + "&pi=" + pi);
}

export const restartDHT = (user, pi) => {
  return axios.get(RESTART_DHT + '?userId=' + user.token + "&pi=" + pi);
}

export const restartPI = (user, pi) => {
  return axios.get(RESTART_PI + '?userId=' + user.token + "&pi=" + pi);
}
