package test.yts.api;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.testng.annotations.Test;

import com.api.core.Service;
import com.api.response.ListMovies;
import com.api.response.MoviesDetails;

import auto.framework.ReportLog;
import auto.framework.TestBase;

public class Get_CastDetails_for_a_movie extends TestBase {
	
	@Test
	public void testCase() throws ClientProtocolException, IOException {
		
		ReportLog.setTestCase("Get Cast details for a movie");
		
		ReportLog.setTestStep("Get List Of movies via API");
		ListMovies movieList = Service.provideMovieList();
		ReportLog.addInfo("Movies Returned by Webservice:"+movieList.getMovieNames());

		ReportLog.setTestStep("Get 1st movie Cast details for 1st movie from above list");
		MoviesDetails moviesDetails = Service.provideMovieDetailsWithoutImageWithCast(movieList.getMovieIds().get(0));
		ReportLog.addInfo("CastDetails:"+moviesDetails.returnCastDetails());
		
	}

}
