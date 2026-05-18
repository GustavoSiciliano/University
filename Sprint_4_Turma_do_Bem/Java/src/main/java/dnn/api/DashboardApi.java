package dnn.api;

import dnn.dao.Conexao;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.*;
import java.util.*;

// GET /api/dashboard/stats — KPIs para o Dashboard do React
@Path("/api/dashboard")
@Produces(MediaType.APPLICATION_JSON)
public class DashboardApi {

    @GET
    @Path("/stats")
    public Response getStats() {
        try (Connection conn = Conexao.conectar()) {
            Map<String, Object> dados = new LinkedHashMap<>();
            dados.put("totalPacientes", qi(conn, "SELECT COUNT(*) FROM tdb_Paciente"));
            dados.put("totalDentistas", qi(conn, "SELECT COUNT(*) FROM tdb_Dentista"));
            dados.put("consultasHoje", qi(conn, "SELECT COUNT(*) FROM tdb_Consulta WHERE TRUNC(data_consulta)=TRUNC(SYSDATE)"));
            dados.put("consultasMes", qi(conn, "SELECT COUNT(*) FROM tdb_Consulta WHERE EXTRACT(MONTH FROM data_consulta)=EXTRACT(MONTH FROM SYSDATE) AND EXTRACT(YEAR FROM data_consulta)=EXTRACT(YEAR FROM SYSDATE)"));
            dados.put("totalArrecadadoMes", qd(conn, "SELECT NVL(SUM(valor),0) FROM tdb_Doacao WHERE EXTRACT(MONTH FROM data_doacao)=EXTRACT(MONTH FROM SYSDATE)"));
            dados.put("campanhasAtivas", qi(conn, "SELECT COUNT(*) FROM tdb_Campanha WHERE ativo='S'"));
            dados.put("materiaisEstoqueBaixo", qi(conn, "SELECT COUNT(*) FROM tdb_Material WHERE quantidade < quantidade_minima"));
            int tot = qi(conn, "SELECT COUNT(*) FROM tdb_Consulta WHERE EXTRACT(MONTH FROM data_consulta)=EXTRACT(MONTH FROM SYSDATE)");
            int fat = qi(conn, "SELECT COUNT(*) FROM tdb_Consulta WHERE status='FALTA' AND EXTRACT(MONTH FROM data_consulta)=EXTRACT(MONTH FROM SYSDATE)");
            dados.put("taxaFalta", tot > 0 ? Math.round((fat * 100.0 / tot) * 10.0) / 10.0 : 0.0);
            dados.put("ultimasConsultas", ultimasConsultas(conn));
            return Response.ok(dados).build();
        } catch (SQLException e) {
            return Response.status(500).entity(Map.of("erro", "Erro no dashboard: " + e.getMessage())).build();
        }
    }

    private int qi(Connection c, String sql) throws SQLException {
        try (PreparedStatement s = c.prepareStatement(sql); ResultSet r = s.executeQuery()) {
            return r.next() ? r.getInt(1) : 0;
        }
    }

    private double qd(Connection c, String sql) throws SQLException {
        try (PreparedStatement s = c.prepareStatement(sql); ResultSet r = s.executeQuery()) {
            return r.next() ? r.getDouble(1) : 0;
        }
    }

    private List<Map<String, Object>> ultimasConsultas(Connection c) throws SQLException {
        List<Map<String, Object>> l = new ArrayList<>();
        String sql = "SELECT c.id_consulta,pac.nome AS paciente,den.nome AS dentista," +
                "TO_CHAR(c.data_consulta,'DD/MM/YYYY') AS data_consulta,c.status " +
                "FROM tdb_Consulta c " +
                "JOIN tdb_Paciente pa ON pa.id_paciente=c.id_paciente " +
                "JOIN tdb_Pessoa pac ON pac.cpf=pa.cpf " +
                "JOIN tdb_Dentista d ON d.cro=c.cro_dentista AND d.cpf=c.cpf_dentista " +
                "JOIN tdb_Pessoa den ON den.cpf=d.cpf " +
                "ORDER BY c.data_consulta DESC FETCH FIRST 5 ROWS ONLY";
        try (PreparedStatement s = c.prepareStatement(sql); ResultSet r = s.executeQuery()) {
            while (r.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("idConsulta", r.getInt("id_consulta"));
                row.put("paciente", r.getString("paciente"));
                row.put("dentista", r.getString("dentista"));
                row.put("dataConsulta", r.getString("data_consulta"));
                row.put("status", r.getString("status"));
                l.add(row);
            }
        }
        return l;
    }
}
