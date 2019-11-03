import { createStore, applyMiddleware } from 'redux';
import '@testing-library/jest-dom/extend-expect';
import React from 'react';
import { render, wait } from '@testing-library/react';
import { Provider } from 'react-redux';
import thunk from 'redux-thunk';
import mainReducer from '../../reducers/main-reducer';
import * as axiosService from '../../services/axios-service';
import LogOnPage from "./LogOnPage";

describe('mock axios-service logon: LogOnPage render tests', () => {
    let store;
    let mockLogOn;
    beforeEach(() => {
        store = createStore(mainReducer, applyMiddleware(thunk));
        mockLogOn = jest.fn();
        axiosService.logOn = mockLogOn;
    });

    afterEach(() => {
        mockLogOn.mockClear();
    });

    test('renders logon page', () => {

        const {container} = render(
            <Provider store={store}>
                <LogOnPage/>
            </Provider>

        )
        expect(
            container.querySelector('div.logOnForm')
        ).toBeInTheDocument();

        expect(
            container.querySelector('input.logonInput')
        ).toBeInTheDocument();

        expect(
            container.querySelectorAll('input.logonInput').length
        ).toBe(2);
    })
});