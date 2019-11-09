import axios from "axios";
import * as constants from "../constants/page-constants";

const loadEndpoint = constants.LOADING_PAGE;
const logOnEndpoint = constants.LOGIN_PAGE;

/*const getEndpoint = endpoint => {
  return process.env.NODE_ENV === "development" ||
    process.env.NODE_ENV === "test"
    ? `http://192.168.1.25:7733${endpoint}`
    : endpoint;
};*/

export const load = () => axios.get(loadEndpoint);

export const logOn = (username, password) => {
  const LogonForm = {
    username: username,
    password: password
  };

  return axios.post(logOnEndpoint, LogonForm);
};
