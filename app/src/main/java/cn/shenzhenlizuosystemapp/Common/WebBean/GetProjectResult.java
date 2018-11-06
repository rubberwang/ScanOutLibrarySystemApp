package cn.shenzhenlizuosystemapp.Common.WebBean;

import java.util.ArrayList;
import java.util.List;

public class GetProjectResult {

    public List<Project> Projects = new ArrayList<>();

    public class Project
    {
        public String ProjectName;

        public String ConnecTionToString;
    }
}
