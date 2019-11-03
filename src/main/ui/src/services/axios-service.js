import axios from 'axios';
import * as constants from '../constants/page-constants'
import {SUMMARY_PAGE} from "../constants/page-constants";
import {LOGIN_PAGE} from "../constants/page-constants";

const loadEndpoint = constants.SUMMARY_PAGE;
const logOnEndpoint = constants.LOGIN_PAGE;

export const load = () =>
    axios.get(loadEndpoint,{ timeout: 10000 });

export const logOn = (username, password) =>
    axios.post(logOnEndpoint, {username: username, password: password},{ timeout: 10000 });
