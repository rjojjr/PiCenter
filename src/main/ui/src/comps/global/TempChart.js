import React, { PureComponent } from 'react';
import {
    LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend,
} from 'recharts';

const TempChart = ({data}) => {
    return (
        <LineChart
            width={500}
            height={300}
            data={data}
            margin={{
                top: 5, right: 30, left: 20, bottom: 5,
            }}
        >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="name" />
            <YAxis />
            <Tooltip />
            <Legend />
            <Line type="monotone" dataKey="Bedroom" stroke="#8884d8" activeDot={{ r: 8 }} />
            <Line type="monotone" dataKey="Living Room" stroke="#82ca9d" />
            <Line type="monotone" dataKey="Server Room" stroke="#ff3346" />
            <Line type="monotone" dataKey="Office" stroke="#fff633" />
            <Line type="monotone" dataKey="Outside" stroke="#ff33ff" />
            <Line type="monotone" dataKey="Heat" stroke="#ff3f33" />
        </LineChart>
    );
};

export default TempChart;