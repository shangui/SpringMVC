package onesun.quartz;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AssessmentQuartzJob {
	private static final Log log = LogFactory.getLog(AssessmentQuartzJob.class);

	public void work() throws Exception {
		Long currentime = System.currentTimeMillis();
		log.info("Start running Quartz Job............");
		log.info("开始的时间为" + currentime);
		Long endtime = System.currentTimeMillis();
		Long time1 = endtime - currentime;
		Date t = new Date(time1);
		log.info("Quartz Job running time:" + t);
	}
}
