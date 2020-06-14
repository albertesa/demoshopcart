package albertesa.sample.prj.controllers;

public class DeleteDocResponse {
	private String docId;
	private long numOfDeleted;
	
	public DeleteDocResponse(String docId, long numOfDeleted) {
		super();
		this.docId = docId;
		this.numOfDeleted = numOfDeleted;
	}

	public String getDocId() {
		return docId;
	}

	public long getNumOfDeleted() {
		return numOfDeleted;
	}
}
