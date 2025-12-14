package proyecto.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import proyecto.entity.Estafa;

public interface EstafaRepository extends JpaRepository<Estafa, Integer>{

    // BUSCAR CASOS REGISTRADOS POR EL USUARIO QUE LO REGISTRA
    List<Estafa> findByUsuarioCodigoUsuario(Integer codigoUsuario);
	
    //TRAER SOLO LAS ESTAFAS CON EL ESTADO SELECCIONADO, EN ESTE CASO 
    //SERÁ UTILIZADO PARA EL ESTADO DE APROBADOS
    List<Estafa> findByEstadoEstafa_CodigoEstadoEstafa(Integer idEstado);

    //LISTA DE TODOS LOS CASOS ORDENADOS POR ID QUE ESTAN APROBADOS    
    List<Estafa> findByEstadoEstafa_CodigoEstadoEstafaOrderByCodigoEstafaDesc(Integer idEstado);

    //LISTA SIMILAR A LA ANTERIOR PERO SOLO DE LOS PRIMEROS 3
    List<Estafa> findTop3ByEstadoEstafa_CodigoEstadoEstafaOrderByCodigoEstafaDesc(Integer codigoEstado);

    //REEMPLAZAREMOS LOS DOS LISTADOS ARRIBA MENCIONADOS POR ALGO MÁS PRÁCTICO
    
    // Lista todos los casos con estado 1 o 2 ordenados por ID descendente
    @Query("""
        SELECT e
        FROM Estafa e
        WHERE e.estadoEstafa.codigoEstadoEstafa IN (1, 2)
        ORDER BY e.codigoEstafa DESC
    """)
    List<Estafa> listarCasosAprobados();

    // Lista los primeros 3 casos con estado 1 o 2 ordenados por ID descendente
    @Query(
        value = "SELECT * FROM estafa e WHERE e.id_estado IN (1, 2) ORDER BY e.id_estafa DESC LIMIT 3",
        nativeQuery = true
    )
    List<Estafa> listarTop3CasosAprobados();
    

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
    
    
    //MÉTODO PARA FILTRAR LOS DATOS POR LOS PARÁMETROS INDICADOS
    @Query("""
            SELECT e FROM Estafa e
            WHERE 
                (:q IS NULL OR :q = '' OR 
                    LOWER(e.tituloCaso) LIKE LOWER(CONCAT('%', :q, '%')) OR
                    LOWER(e.descripcionEstafa) LIKE LOWER(CONCAT('%', :q, '%'))
                )
            AND (:implicado IS NULL OR :implicado = '' OR 
                    LOWER(e.ciberdelincuente) LIKE LOWER(CONCAT('%', :implicado, '%'))
                )
            AND (:modalidad IS NULL OR e.modalidadEstafa.codigoModalidadEstafa = :modalidad)
            AND (:medio IS NULL OR e.medioEstafa.codigoMedioEstafa = :medio)
            AND e.estadoEstafa.codigoEstadoEstafa IN (1, 2)
            ORDER BY e.codigoEstafa DESC
        """)
        List<Estafa> buscarCasos(
                @Param("q") String q,
                @Param("implicado") String implicado,
                @Param("modalidad") Integer modalidad,
                @Param("medio") Integer medio
        );
}
