export class IdGenerator {

	private static CONTAINER_ID_LENGTH: number = 10;

	/**
	 * https://stackoverflow.com/questions/1349404/generate-random-string-characters-in-javascript 
	 * @returns 
	 */
	public static generateContainerId():string {
		var result           = '';
		var characters       = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
		var charactersLength = characters.length;
		for ( var i = 0; i < IdGenerator.CONTAINER_ID_LENGTH; i++ ) {
			result += characters.charAt(Math.floor(Math.random() * charactersLength));
		}
		return result;
	}
}