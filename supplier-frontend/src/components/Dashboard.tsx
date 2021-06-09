import React from 'react';

import BenchmarkControl from './BenchmarkControl';

type DashboardProps = {};
type DashboardState = {};

class Dashboard extends React.Component<DashboardProps, DashboardState> {

	constructor(props: DashboardProps) {
        super(props);
	}
	
    render() {
        return (
            <div>
				<BenchmarkControl/>
            </div>
        );
    }
}

export default Dashboard;