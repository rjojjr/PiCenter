import {createStore, applyMiddleware} from 'redux';
import '@testing-library/jest-dom/extend-expect';
import React from 'react';
import {render, wait, fireEvent} from '@testing-library/react';
import {Provider} from 'react-redux';
import thunk from 'redux-thunk';
import mainReducer from '../../reducers/main-reducer';
import * as axiosService from '../../services/axios-service';
import LogOnPage from "./LogOnPage";
import LogOnPageContainer from "./LogOnPageContainer";
import AppContainer from '../AppContainer';
import * as constants from '../../constants/page-constants'

describe('redux integration: LogOnPageContainer render tests', () => {
    
    let store;
    let mockLogOn;

    const initialState = {
        user: {},
        isLoading: false,
        isError: false,
        message: '',
        isShowMsg: false,
        errorMsg: '',
        isLoggedOn: false,
        isLoggingOn: false
    };

    beforeEach(() => {
        store = createStore(mainReducer, initialState, applyMiddleware(thunk));
    });
    afterEach(() => {

    });

    test('renders logon page', async () => {

        const { container } = render(
            <Provider store={store}>
                <AppContainer />
            </Provider>
        )

        await wait();

        expect(
            container.querySelector('div.logOnPage')
        ).toBeInTheDocument();

        expect(
            container.querySelector('input.logonInput')
        ).toBeInTheDocument();

        expect(
            container.querySelector('button')
        ).toBeInTheDocument();

        expect(
            container.querySelectorAll('input.logonInput').length
        ).toBe(2);

    });

});

describe('mock axios-service logon: LogOnPageContainer render tests', () => {
    let store;
    let mockLogOn;

    const initialState = {
        user: {},
        isLoading: false,
        isError: false,
        message: '',
        isShowMsg: false,
        errorMsg: '',
        isLoggedOn: false,
        isLoggingOn: false
    };

    beforeEach(() => {
        store = createStore(mainReducer, initialState, applyMiddleware(thunk));
        mockLogOn = jest.fn();
        axiosService.logOn = mockLogOn;
    });

    afterEach(() => {
        mockLogOn.mockClear();
    });

    test('mock successful logon returns valid user', async () => {
        
        mockLogOn.mockReturnValue(Promise.resolve({ data:{ user: {userName: 'admin', page: constants.SUMMARY_PAGE}} }))

        const {container} = render(
            <Provider store={store}>
                <AppContainer />
            </Provider>
        )

        await wait();

        expect(
            container.querySelector('div.logOnPage')
        ).toBeInTheDocument();

        expect(
            container.querySelector('input.logonInput')
        ).toBeInTheDocument();

        expect(
            container.querySelector('button')
        ).toBeInTheDocument();

        expect(
            container.querySelectorAll('input.logonInput').length
        ).toBe(2);

        const logOnButton = container.querySelectorAll('button')[0];

        fireEvent.click(logOnButton);

        await wait();

        expect(
            container.querySelector('div.summaryPage')
        ).toBeInTheDocument();
    });

    test('mock unsuccessful logon renders message', async () => {

        mockLogOn.mockReturnValue(Promise.resolve('Network Error'))

        const {container} = render(
            <Provider store={store}>
                <AppContainer />
            </Provider>
        )

        await wait();

        expect(
            container.querySelector('input.logonInput')
        ).toBeInTheDocument();

        expect(
            container.querySelector('button')
        ).toBeInTheDocument();

        expect(
            container.querySelectorAll('input.logonInput').length
        ).toBe(2);

        const logOnButton = container.querySelectorAll('button')[0];

        fireEvent.click(logOnButton);

        await wait();

        expect(
            container.querySelector('p.message')
        ).toBeInTheDocument();

        expect(
            container.querySelector('input.logonInput')
        ).toBeInTheDocument();

        expect(
            container.querySelector('button')
        ).toBeInTheDocument();

        expect(
            container.querySelectorAll('input.logonInput').length
        ).toBe(2);
    });

});

describe('LogOnPage event tests', () => {
    test('logon page events work', () => {

        const mockResetIsShowMsg = jest.fn();
        const mockLogOn = jest.fn();

        const {container} = render(
            <LogOnPage user={{userName: undefined}} logOn={mockLogOn} resetIsShowMsg={mockResetIsShowMsg} isShowMsg={false} message={''} isLoggingOn={false}/>
        );

        expect(
            container.querySelector('div.logOnPage')
        ).toBeInTheDocument();

        const userNameInput = container.querySelectorAll('input')[0];
        const passwordInput = container.querySelectorAll('input')[1];

        fireEvent.change(userNameInput, {target: {value: 'abcd'}});

        expect(
            mockResetIsShowMsg
        ).toHaveBeenCalledTimes(1);

        fireEvent.change(passwordInput, {target: {value: 'abcd'}});

        expect(
            mockResetIsShowMsg
        ).toHaveBeenCalledTimes(2);

        const logOnButton = container.querySelectorAll('button')[0];

        fireEvent.click(logOnButton);

        expect(
            mockLogOn
        ).toHaveBeenCalledWith('abcd', 'abcd');

    });
});