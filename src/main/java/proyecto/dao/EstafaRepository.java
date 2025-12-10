package proyecto.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import proyecto.entity.Estafa;

public interface EstafaRepository extends JpaRepository<Estafa, Integer>{

    // BUSCAR CASOS REGISTRADOS POR EL USUARIO QUE LO REGISTRA
    List<Estafa> findByUsuarioCodigoUsuario(Integer codigoUsuario);
	
    //TRAER SOLO LAS ESTAFAS CON EL ESTADO SELECCIONADO, EN ESTE CASO 
    //SERÁ UTILIZADO PARA EL ESTADO DE APROBADOS
    List<Estafa> findByEstadoEstafa_CodigoEstadoEstafa(Integer idEstado);


    // =============================
    //        GRÁFICOS BÁSICOS
    // =============================

    @Query("SELECT m.nombreMedioEstafa, COUNT(e) " +
           "FROM Estafa e JOIN e.medioEstafa m " +
           "GROUP BY m.nombreMedioEstafa")
    List<Object[]> conteoPorMedio();


    @Query("SELECT mo.nombreModalidadEstafa, COUNT(e) " +
           "FROM Estafa e JOIN e.modalidadEstafa mo " +
           "GROUP BY mo.nombreModalidadEstafa")
    List<Object[]> conteoPorModalidad();



    // =============================
    //        GRÁFICOS AVANZADOS
    // =============================

    // 1️⃣ Reportes por AÑO
    @Query("""
           SELECT YEAR(e.fechaReporte), COUNT(e)
           FROM Estafa e
           GROUP BY YEAR(e.fechaReporte)
           ORDER BY YEAR(e.fechaReporte)
           """)
    List<Object[]> conteoPorAnio();


    // 2️⃣ Reportes por MES del AÑO ACTUAL
    @Query("""
           SELECT MONTH(e.fechaReporte), COUNT(e)
           FROM Estafa e
           WHERE YEAR(e.fechaReporte) = YEAR(CURRENT_DATE)
           GROUP BY MONTH(e.fechaReporte)
           ORDER BY MONTH(e.fechaReporte)
           """)
    List<Object[]> conteoPorMesActual();


    // 3️⃣ Total por AÑO
    @Query("""
           SELECT COUNT(e)
           FROM Estafa e
           WHERE YEAR(e.fechaReporte) = ?1
           """)
    long totalPorAnio(int anio);


    // 4️⃣ Total por MES específico
    @Query("""
           SELECT COUNT(e)
           FROM Estafa e
           WHERE YEAR(e.fechaReporte) = ?1
             AND MONTH(e.fechaReporte) = ?2
           """)
    long totalPorMes(int anio, int mes);


    // 5️⃣ Reportes por MES según AÑO
    @Query("""
           SELECT MONTH(e.fechaReporte), COUNT(e)
           FROM Estafa e
           WHERE YEAR(e.fechaReporte) = ?1
           GROUP BY MONTH(e.fechaReporte)
           ORDER BY MONTH(e.fechaReporte)
           """)
    List<Object[]> reportesPorMes(int anio);


 // 📈 Tendencia histórica (AÑO - MES)
    @Query("SELECT YEAR(e.fechaReporte), MONTH(e.fechaReporte), COUNT(e) " +
           "FROM Estafa e " +
           "GROUP BY YEAR(e.fechaReporte), MONTH(e.fechaReporte) " +
           "ORDER BY YEAR(e.fechaReporte), MONTH(e.fechaReporte)")
    List<Object[]> tendenciaHistorica();
    
}
