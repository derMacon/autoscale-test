import * as PgDriver from 'pg';
import * as fs from 'fs';

import { ResultWrapper } from '../model/ResultWrapper';

export class PersistenceService {

	private readonly CONNECTED_LOG_MSG: string =
'+--------------------------+\n\
| connected to postgres db |\n\
+--------------------------+\n'

	private readonly ERROR_LOG_MSG: string =
'+-------------------------+\n\
| could not connect to db |\n\
+-------------------------+\n'


	private dbClient: PgDriver.Pool;

	constructor() {
		this.dbClient = new PgDriver.Pool({
			host: process.env.PG_HOSTNAME,
			database: process.env.PG_DATABASE_NAME,
			user: process.env.PG_USER_NAME,
			max: 20,
			idleTimeoutMillis: 30000,
  			connectionTimeoutMillis: 2000,
			password: process.env.PG_USER_PASSWORD,
			port: Number(process.env.PG_DATABASE_PORT),
		});

		this.dbClient.connect(err => {
			if (err) {
				console.log(this.ERROR_LOG_MSG);
			} else {
				console.log(this.CONNECTED_LOG_MSG);
			}
		});

		// load schema file
		fs.readFile("./schema.sql", (error: any, data: any) => {
			if (error) {
				throw error;
			}

			let query: string = data.toString();
			// let query: string = data.toString().replace(/(\r\n|\n|\r|\t)/gm, "");

			// query database 
			this.dbClient.query(query, (err, res) => {
				if (err) {
					console.error(err);
					return;
				}
				// console.log('schema execution successfull', res);
			});


		});

	}

	saveResult(result: ResultWrapper): void {

		// console.log("persist obj: ", result);

		const query = `
		INSERT INTO payment (
			content, 
			extracted_element, 
			sent_timestamp, 
			received_timestamp, 
			processed_timestamp
		) VALUES (
			'${result.message}',
			'${result.extractedElem}',
			'${this.transformDate(result.receivedTimestamp!)}',
			'${this.transformDate(result.receivedTimestamp!)}',
			'${this.transformDate(result.processedTimestamp!)}'
		);`;

		this.dbClient.query(query, (err, res) => {
			if (err) {
				console.error(err);
				return;
			}
			console.log(' -> data insert successfully');
		});
	}

	transformDate(inputDate: Date): string {
		// console.log('date: ', inputDate)
		// return inputDate.getMilliseconds();
		// return inputDate.toLocaleDateString() + " " + inputDate.toLocaleTimeString() + ":" + inputDate.getMilliseconds();
		return inputDate.toISOString();
	}

}