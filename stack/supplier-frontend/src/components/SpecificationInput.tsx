import React, { Component } from 'react';

type SpecificationInputProps = {};
type SpecificationInputState = {};

class Dashboard extends React.Component<SpecificationInputProps, SpecificationInputState> {

	constructor(props: SpecificationInputProps) {
        super(props);
	}
	
    render() {
        return (
            <div>
				Specification input
            </div>
        );
    }
}