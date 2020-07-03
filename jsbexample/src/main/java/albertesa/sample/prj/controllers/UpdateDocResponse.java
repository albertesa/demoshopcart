package albertesa.sample.prj.controllers;

public class UpdateDocResponse {
	private String docId;
	private long numOfUpdated;
	
	public UpdateDocResponse(String docId, long numOfUpdated) {
		super();
		this.docId = docId;
		this.numOfUpdated = numOfUpdated;
	}

	public String getDocId() {
		return docId;
	}

	public long getNumOfUpdated() {
		return numOfUpdated;
	}
}
