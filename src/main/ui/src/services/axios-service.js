import axios from 'axios';
import * as constants from '../constants/page-constants'
import {SUMMARY_PAGE} from "../constants/page-constants";
import {LOGIN_PAGE} from "../constants/page-constants";

const loadEndpoint = constants.SUMMARY_PAGE;
const logOnEndpoint = constants.LOGIN_PAGE;

const getEndpoint = (endpoint) => {
    return (process.env.NODE_ENV === dev
        ? `http://192.168.1.25:7733${endpoint}`
        : endpoint);
}

export const load = () =>
    axios.get(getEndpoint(loadEndpoint),{ timeout: 10000 });

export const logOn = (username, password) =>
    axios.post(getEndpoint(logOnEndpoint), {username: username, password: password},{ timeout: 10000 });
